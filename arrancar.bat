@echo off
echo ==========================================
echo    INICIANDO MICROSERVICIOS CHIPPING
echo ==========================================

set RUTA=C:\Users\Nicolas\OneDrive\Desktop\Duoc\FullStack\Proyecto\Chipping definitivo pirss\Chipping definitivo

echo.
echo [1/10] Arrancando proveedores (8091)...
start "proveedores" cmd /k "cd /d "%RUTA%\proveedores\proveedores" && mvnw spring-boot:run"
timeout /t 5 /nobreak >nul

echo [2/10] Arrancando productos (8090)...
start "productos" cmd /k "cd /d "%RUTA%\productos\productos\productos" && mvnw spring-boot:run"
timeout /t 5 /nobreak >nul

echo [3/10] Arrancando usuarios (8093)...
start "usuarios" cmd /k "cd /d "%RUTA%\usuarios\usuarios" && mvnw spring-boot:run"
timeout /t 5 /nobreak >nul

echo [4/10] Arrancando inventario (8092)...
start "inventario" cmd /k "cd /d "%RUTA%\inventario\inventario" && mvnw spring-boot:run"
timeout /t 5 /nobreak >nul

echo [5/10] Arrancando notificaciones (8095)...
start "notificaciones" cmd /k "cd /d "%RUTA%\notificaciones\notificaciones" && mvnw spring-boot:run"
timeout /t 5 /nobreak >nul

echo [6/10] Arrancando carrocompra (8096)...
start "carrocompra" cmd /k "cd /d "%RUTA%\carrocompra\carrocompra" && mvnw spring-boot:run"
timeout /t 8 /nobreak >nul

echo [7/10] Arrancando pedido (8094)...
start "pedido" cmd /k "cd /d "%RUTA%\pedido\pedido" && mvnw spring-boot:run"
timeout /t 8 /nobreak >nul

echo [8/10] Arrancando pagos (8097)...
start "pagos" cmd /k "cd /d "%RUTA%\pagos\pagos" && mvnw spring-boot:run"
timeout /t 8 /nobreak >nul

echo [9/10] Arrancando envios (8098)...
start "envios" cmd /k "cd /d "%RUTA%\envios\envios" && mvnw spring-boot:run"
timeout /t 8 /nobreak >nul

echo [10/10] Arrancando resenas (8099)...
start "resenas" cmd /k "cd /d "%RUTA%\resenas\resenas" && mvnw spring-boot:run"
timeout /t 5 /nobreak >nul

echo.
echo ==========================================
echo    TODOS LOS SERVICIOS INICIADOS
echo ==========================================
echo.
echo Puertos en uso:
echo   proveedores    -^> http://localhost:8091
echo   productos      -^> http://localhost:8090
echo   usuarios       -^> http://localhost:8093
echo   inventario     -^> http://localhost:8092
echo   notificaciones -^> http://localhost:8095
echo   carrocompra    -^> http://localhost:8096
echo   pedido         -^> http://localhost:8094
echo   pagos          -^> http://localhost:8097
echo   envios         -^> http://localhost:8098
echo   resenas        -^> http://localhost:8099
echo.
pause