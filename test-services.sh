#!/bin/bash
# test-services.sh - Versión corregida

echo "🧪 Probando servicios de Banking Microservices"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

TOTAL_TESTS=0
PASSED_TESTS=0

test_service() {
    local name=$1
    local url=$2
    local expected=${3:-200}
    
    TOTAL_TESTS=$((TOTAL_TESTS + 1))
    echo -n "   $name: "
    
    RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" "$url" 2>/dev/null)
    
    if [ "$RESPONSE" = "$expected" ]; then
        echo -e "${GREEN}✅ OK (HTTP $RESPONSE)${NC}"
        PASSED_TESTS=$((PASSED_TESTS + 1))
    else
        echo -e "${RED}❌ ERROR (HTTP $RESPONSE)${NC}"
    fi
}

# Health Checks
echo ""
echo "📊 Health Checks - Servicios Directos:"
test_service "Eureka Server" "http://localhost:8761/actuator/health"
test_service "API Gateway" "http://localhost:8080/actuator/health"
test_service "Account Service" "http://localhost:8081/actuator/health"
test_service "Transfer Service" "http://localhost:8082/actuator/health"

# Endpoints de negocio via Gateway (los correctos)
echo ""
echo "🚪 Endpoints via Gateway:"
test_service "GET /api/cuentas" "http://localhost:8080/api/cuentas"
test_service "GET /api/transferencias" "http://localhost:8080/api/transferencias"

# Servicios en Eureka
echo ""
echo "📋 Servicios en Eureka:"
EUREKA_APPS=$(curl -s http://localhost:8761/eureka/apps 2>/dev/null)
for service in "ACCOUNT-SERVICE" "TRANSFER-SERVICE" "API-GATEWAY"; do
    TOTAL_TESTS=$((TOTAL_TESTS + 1))
    if echo "$EUREKA_APPS" | grep -q "$service"; then
        echo -e "   ${GREEN}✅ $service registrado${NC}"
        PASSED_TESTS=$((PASSED_TESTS + 1))
    else
        echo -e "   ${RED}❌ $service NO registrado${NC}"
    fi
done

# Kafka
echo ""
echo "📨 Kafka:"
TOTAL_TESTS=$((TOTAL_TESTS + 1))
if docker ps | grep -q "kafka"; then
    echo -e "   ${GREEN}✅ Kafka está corriendo${NC}"
    PASSED_TESTS=$((PASSED_TESTS + 1))
else
    echo -e "   ${RED}❌ Kafka NO está corriendo${NC}"
fi

# Resumen
echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

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
echo "   🌐 Eureka:    http://localhost:8761"
echo "   🚪 Gateway:   http://localhost:8080"
echo "   💳 Account:   http://localhost:8081/api/cuentas"
echo "   💸 Transfer:  http://localhost:8082/api/transferencias"
echo ""
echo "🧪 Pruebas rápidas:"
echo "   curl http://localhost:8080/api/cuentas"
echo "   curl http://localhost:8080/api/transferencias"