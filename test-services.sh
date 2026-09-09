#!/bin/bash
# test-services.sh - Pruebas rápidas de los servicios

echo "🧪 Probando servicios de Banking Microservices"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Contador de pruebas
TOTAL_TESTS=0
PASSED_TESTS=0

test_service() {
    local name=$1
    local url=$2
    local expected=${3:-200}
    
    TOTAL_TESTS=$((TOTAL_TESTS + 1))
    echo -n "   $name: "
    
    RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" $url 2>/dev/null)
    
    if [ "$RESPONSE" = "$expected" ]; then
        echo -e "${GREEN}✅ OK (HTTP $RESPONSE)${NC}"
        PASSED_TESTS=$((PASSED_TESTS + 1))
        return 0
    else
        echo -e "${RED}❌ ERROR (HTTP $RESPONSE)${NC}"
        return 1
    fi
}

# ============================================
# 1. HEALTH CHECKS - Servicios Directos
# ============================================
echo ""
echo "📊 Health Checks - Servicios Directos:"
test_service "Eureka Server" "http://localhost:8761/actuator/health"
test_service "API Gateway" "http://localhost:8080/actuator/health"
test_service "Account Service" "http://localhost:8081/actuator/health"  # ← Corregido: sin /api/cuentas
test_service "Transfer Service" "http://localhost:8082/actuator/health"  # ← Corregido: sin /api/transferencias

# ============================================
# 3. EUREKA REGISTRY
# ============================================
echo ""
echo "📋 Servicios registrados en Eureka:"

EUREKA_APPS=$(curl -s http://localhost:8761/eureka/apps 2>/dev/null)

if echo "$EUREKA_APPS" | grep -q "ACCOUNT-SERVICE"; then
    echo -e "   ${GREEN}✅ ACCOUNT-SERVICE registrado${NC}"
    PASSED_TESTS=$((PASSED_TESTS + 1))
else
    echo -e "   ${RED}❌ ACCOUNT-SERVICE NO registrado${NC}"
fi
TOTAL_TESTS=$((TOTAL_TESTS + 1))

if echo "$EUREKA_APPS" | grep -q "TRANSFER-SERVICE"; then
    echo -e "   ${GREEN}✅ TRANSFER-SERVICE registrado${NC}"
    PASSED_TESTS=$((PASSED_TESTS + 1))
else
    echo -e "   ${RED}❌ TRANSFER-SERVICE NO registrado${NC}"
fi
TOTAL_TESTS=$((TOTAL_TESTS + 1))

if echo "$EUREKA_APPS" | grep -q "API-GATEWAY"; then
    echo -e "   ${GREEN}✅ API-GATEWAY registrado${NC}"
    PASSED_TESTS=$((PASSED_TESTS + 1))
else
    echo -e "   ${RED}❌ API-GATEWAY NO registrado${NC}"
fi
TOTAL_TESTS=$((TOTAL_TESTS + 1))

# ============================================
# 4. DETALLE DE INSTANCIAS
# ============================================
echo ""
echo "📊 Detalle de instancias registradas:"

echo -e "${BLUE}   Account Service:${NC}"
ACCOUNT_INFO=$(curl -s http://localhost:8761/eureka/apps/ACCOUNT-SERVICE 2>/dev/null)
if [ -n "$ACCOUNT_INFO" ]; then
    STATUS=$(echo "$ACCOUNT_INFO" | grep -o "<status>[^<]*</status>" | sed 's/<status>\(.*\)<\/status>/\1/')
    echo "      Status: $STATUS"
else
    echo "      ❌ No disponible"
fi

echo ""
echo -e "${BLUE}   Transfer Service:${NC}"
TRANSFER_INFO=$(curl -s http://localhost:8761/eureka/apps/TRANSFER-SERVICE 2>/dev/null)
if [ -n "$TRANSFER_INFO" ]; then
    STATUS=$(echo "$TRANSFER_INFO" | grep -o "<status>[^<]*</status>" | sed 's/<status>\(.*\)<\/status>/\1/')
    echo "      Status: $STATUS"
else
    echo "      ❌ No disponible"
fi

echo ""
echo -e "${BLUE}   API Gateway:${NC}"
GATEWAY_INFO=$(curl -s http://localhost:8761/eureka/apps/API-GATEWAY 2>/dev/null)
if [ -n "$GATEWAY_INFO" ]; then
    STATUS=$(echo "$GATEWAY_INFO" | grep -o "<status>[^<]*</status>" | sed 's/<status>\(.*\)<\/status>/\1/')
    echo "      Status: $STATUS"
else
    echo "      ❌ No disponible"
fi

# ============================================
# 5. RUTAS DEL GATEWAY
# ============================================
echo ""
echo "🛤️  Rutas configuradas en el Gateway:"

ROUTES=$(curl -s http://localhost:8080/actuator/gateway/routes 2>/dev/null)
if [ -n "$ROUTES" ]; then
    if command -v jq &> /dev/null; then
        echo "$ROUTES" | jq -r '.[] | "   🔹 \(.route_id) → \(.predicate)"' 2>/dev/null
    else
        echo "$ROUTES" | grep -E "route_id|predicate" | sed 's/.*"route_id": "\(.*\)".*/   🔹 \1/' | head -10
    fi
else
    echo "   ❌ No se pudo obtener"
fi

# ============================================
# 6. PUERTOS Y ACCESOS
# ============================================
echo ""
echo "🔌 Puertos expuestos:"
echo "   Account DB: 5433"
echo "   Transfer DB: 5434"
echo "   Account Service: 8081"
echo "   Transfer Service: 8082"
echo "   API Gateway: 8080"
echo "   Eureka Server: 8761"

# ============================================
# 7. RESUMEN DE PRUEBAS
# ============================================
echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

# Calcular porcentaje
PERCENTAGE=$((PASSED_TESTS * 100 / TOTAL_TESTS))

if [ $PASSED_TESTS -eq $TOTAL_TESTS ]; then
    echo -e "${GREEN}✅ TODAS LAS PRUEBAS PASARON ($PASSED_TESTS/$TOTAL_TESTS)${NC}"
elif [ $PERCENTAGE -ge 80 ]; then
    echo -e "${YELLOW}⚠️  ALGUNAS PRUEBAS FALLARON ($PASSED_TESTS/$TOTAL_TESTS)${NC}"
else
    echo -e "${RED}❌ VARIAS PRUEBAS FALLARON ($PASSED_TESTS/$TOTAL_TESTS)${NC}"
fi

echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "📌 URLs de acceso:"
echo "   🌐 Eureka Dashboard:  http://localhost:8761"
echo "   🚪 API Gateway:        http://localhost:8080"
echo "   💳 Account Service:    http://localhost:8081"
echo "   💸 Transfer Service:   http://localhost:8082"
echo ""
echo "📋 Comandos útiles:"
echo "   docker-compose ps               # Ver estado"
echo "   docker-compose logs -f          # Ver logs en tiempo real"
echo "   docker-compose logs api-gateway # Ver logs del Gateway"
echo "   docker-compose down             # Detener servicios"
echo "   docker-compose up -d            # Levantar servicios"
echo ""
echo "📊 Health Checks (comandos directos):"
echo "   curl http://localhost:8081/actuator/health"
echo "   curl http://localhost:8082/actuator/health"
echo "   curl http://localhost:8080/api/cuentas/actuator/health"
echo "   curl http://localhost:8080/api/transferencias/actuator/health"