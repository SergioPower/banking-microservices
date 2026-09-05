#!/bin/bash
# test-services.sh - Pruebas rápidas de los servicios

echo "🧪 Probando servicios de Banking Microservices"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

test_service() {
    local name=$1
    local url=$2
    local expected=$3
    
    echo -n "   $name: "
    RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" $url 2>/dev/null)
    
    if [ "$RESPONSE" = "$expected" ]; then
        echo -e "${GREEN}✅ OK (HTTP $RESPONSE)${NC}"
        return 0
    else
        echo -e "${RED}❌ ERROR (HTTP $RESPONSE)${NC}"
        return 1
    fi
}

# 1. Health Checks
echo ""
echo "📊 Health Checks:"
test_service "Eureka Server" "http://localhost:8761/actuator/health" "200"
test_service "API Gateway" "http://localhost:8080/actuator/health" "200"
test_service "Account Service" "http://localhost:8081/api/accounts/actuator/health" "200"
test_service "Transfer Service" "http://localhost:8082/api/transfers/actuator/health" "200"

# 2. Eureka Registry
echo ""
echo "📋 Servicios en Eureka:"
EUREKA_APPS=$(curl -s http://localhost:8761/eureka/apps)

if echo "$EUREKA_APPS" | grep -q "ACCOUNT-SERVICE"; then
    echo -e "   ${GREEN}✅ ACCOUNT-SERVICE registrado${NC}"
else
    echo -e "   ${RED}❌ ACCOUNT-SERVICE NO registrado${NC}"
fi

if echo "$EUREKA_APPS" | grep -q "TRANSFER-SERVICE"; then
    echo -e "   ${GREEN}✅ TRANSFER-SERVICE registrado${NC}"
else
    echo -e "   ${RED}❌ TRANSFER-SERVICE NO registrado${NC}"
fi

# 3. API Gateway Tests
echo ""
echo "🚪 API Gateway Tests:"
test_service "Account via Gateway" "http://localhost:8080/api/accounts/actuator/health" "200"
test_service "Transfer via Gateway" "http://localhost:8080/api/transfers/actuator/health" "200"

# 4. Instancias registradas
echo ""
echo "📊 Detalle de instancias:"
echo "   Account Service:"
curl -s http://localhost:8761/eureka/apps/ACCOUNT-SERVICE | grep -E "<instanceId>|<hostName>" | sed 's/^[ \t]*//'

echo ""
echo "   Transfer Service:"
curl -s http://localhost:8761/eureka/apps/TRANSFER-SERVICE | grep -E "<instanceId>|<hostName>" | sed 's/^[ \t]*//'

echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "✅ Pruebas completadas"
echo ""
echo "📌 URLs:"
echo "   Eureka: http://localhost:8761"
echo "   Gateway: http://localhost:8080"