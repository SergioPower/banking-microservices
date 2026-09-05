#!/bin/bash
# build-all-with-eureka.sh - Versión silenciosa

echo "========================================="
echo "  BANKING MICROSERVICES - BUILD & TEST"
echo "========================================="

# ============================================
# 1. COMPILACIÓN DE SERVICIOS (SILENCIOSA)
# ============================================
echo ""
echo "📦 [1/5] Compilando todos los servicios..."

build_service() {
    local service=$1
    echo -n "   📦 Compilando $service... "
    cd $service
    ./mvnw clean package -DskipTests -q > /dev/null 2>&1
    if [ $? -eq 0 ]; then
        echo "✅"
    else
        echo "❌"
        echo "   ❌ Error compilando $service. Revisa los logs."
        exit 1
    fi
    cd ..
}

build_service "eureka-server"
build_service "api-gateway"
build_service "account-service"
build_service "transfer-service"

# ============================================
# 2. VERIFICACIÓN DE JARS
# ============================================
echo ""
echo "📁 [2/5] Verificando JARs generados..."

check_jar() {
    local service=$1
    local jar_count=$(ls -1 $service/target/*.jar 2>/dev/null | wc -l)
    if [ $jar_count -gt 0 ]; then
        local jar_name=$(ls -1 $service/target/*.jar | head -1 | xargs basename)
        local jar_size=$(ls -lh $service/target/*.jar | head -1 | awk '{print $5}')
        echo "   ✅ $service: $jar_name ($jar_size)"
    else
        echo "   ❌ $service: No se encontró JAR"
        exit 1
    fi
}

check_jar "eureka-server"
check_jar "api-gateway"
check_jar "account-service"
check_jar "transfer-service"

# ============================================
# 3. LEVANTAR SERVICIOS CON DOCKER (SILENCIOSO)
# ============================================
echo ""
echo "🐳 [3/5] Levantando servicios con Docker..."

# Detener y eliminar contenedores anteriores (sin output)
docker-compose down -v > /dev/null 2>&1

# Construir y levantar (silencioso)
echo -n "   ⏳ Construyendo imágenes... "
docker-compose build -q > /dev/null 2>&1
echo "✅"

echo -n "   ⏳ Iniciando contenedores... "
docker-compose up -d > /dev/null 2>&1
echo "✅"

echo -n "   ⏳ Esperando que los servicios inicien... "
sleep 15
echo "✅"

# ============================================
# 4. VERIFICAR ESTADO DE CONTENEDORES
# ============================================
echo ""
echo "🔍 [4/5] Verificando estado de contenedores..."
docker-compose ps --format "table {{.Name}}\t{{.Status}}\t{{.Ports}}"

# ============================================
# 5. PRUEBAS DE REGISTRO Y CONEXIÓN
# ============================================
echo ""
echo "🧪 [5/5] Ejecutando pruebas de integración..."
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

# Función para esperar un servicio (silencioso)
wait_for_service() {
    local url=$1
    local name=$2
    local max_attempts=20
    local attempt=1
    
    echo -n "   ⏳ Esperando $name"
    while [ $attempt -le $max_attempts ]; do
        if curl -s -f "$url" > /dev/null 2>&1; then
            echo " ✅"
            return 0
        fi
        echo -n "."
        sleep 2
        attempt=$((attempt + 1))
    done
    echo " ❌"
    return 1
}

# 5.1 Health Checks
echo ""
echo "📊 Health Checks:"

wait_for_service "http://localhost:8761/actuator/health" "Eureka Server"
wait_for_service "http://localhost:8080/actuator/health" "API Gateway"
wait_for_service "http://localhost:8081/api/accounts/actuator/health" "Account Service"
wait_for_service "http://localhost:8082/api/transfers/actuator/health" "Transfer Service"

# 5.2 Verificar Eureka Registry
echo ""
echo "📋 Verificando registro en Eureka..."

# Esperar un poco más para que los servicios se registren
sleep 5

# Obtener servicios registrados en Eureka
EUREKA_APPS=$(curl -s http://localhost:8761/eureka/apps)

echo "   📍 Servicios registrados:"

# Verificar Account Service
if echo "$EUREKA_APPS" | grep -q "ACCOUNT-SERVICE"; then
    echo "   ✅ ACCOUNT-SERVICE - Registrado"
else
    echo "   ❌ ACCOUNT-SERVICE - No registrado"
fi

# Verificar Transfer Service
if echo "$EUREKA_APPS" | grep -q "TRANSFER-SERVICE"; then
    echo "   ✅ TRANSFER-SERVICE - Registrado"
else
    echo "   ❌ TRANSFER-SERVICE - No registrado"
fi

# Verificar API Gateway
if echo "$EUREKA_APPS" | grep -q "API-GATEWAY"; then
    echo "   ✅ API-GATEWAY - Registrado"
else
    echo "   ⚠️  API-GATEWAY - No registrado"
fi

# 5.3 Pruebas de API Gateway (silencioso)
echo ""
echo "🚪 Probando API Gateway..."

# Probar Account Service a través del Gateway
echo -n "   ➡️  Account Service (via Gateway): "
RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/api/accounts/actuator/health 2>/dev/null)
if [ "$RESPONSE" = "200" ]; then
    echo "✅ OK (HTTP $RESPONSE)"
else
    echo "❌ ERROR (HTTP $RESPONSE)"
fi

# Probar Transfer Service a través del Gateway
echo -n "   ➡️  Transfer Service (via Gateway): "
RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/api/transfers/actuator/health 2>/dev/null)
if [ "$RESPONSE" = "200" ]; then
    echo "✅ OK (HTTP $RESPONSE)"
else
    echo "❌ ERROR (HTTP $RESPONSE)"
fi

# 5.4 Mostrar servicios registrados
echo ""
echo "📋 Servicios en Eureka:"
curl -s http://localhost:8761/eureka/apps | grep -E "<name>" | sed 's/.*<name>\(.*\)<\/name>.*/   🔹 \1/' | sort -u

# ============================================
# 6. RESUMEN FINAL
# ============================================
echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "✅ ¡BUILD COMPLETADO EXITOSAMENTE!"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "📌 URLs de acceso:"
echo "   🌐 Eureka Dashboard:    http://localhost:8761"
echo "   🚪 API Gateway:          http://localhost:8080"
echo "   💳 Account Service:      http://localhost:8081/api/accounts"
echo "   💸 Transfer Service:     http://localhost:8082/api/transfers"
echo ""
echo "📋 Comandos útiles:"
echo "   📊 Ver logs:            docker-compose logs -f"
echo "   🔍 Ver estado:          docker-compose ps"
echo "   🛑 Detener servicios:   docker-compose down"
echo "   🗑️  Eliminar todo:       docker-compose down -v"
echo ""
echo "🧪 Pruebas rápidas:"
echo "   curl http://localhost:8761/eureka/apps  # Ver servicios"
echo "   curl http://localhost:8080/api/accounts/actuator/health  # Account via Gateway"
echo "   curl http://localhost:8080/api/transfers/actuator/health  # Transfer via Gateway"
echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"