# Flujo end-to-end BikeLab (Swagger UI)

### 1 Registrar y autenticar a Bruno
1. **POST `/api/iam/auth/register`** → *Try it out* y envía:
   ```json
   {
     "email": "bruno.provider@demo.io",
     "password": "Bruno#2024",
     "firstName": "Bruno",
     "lastName": "Provider"
   }
   ```

2. **POST `/api/iam/auth/login`** con:

   ```json
   { "email": "bruno.provider@demo.io", "password": "Bruno#2024" }
   ```

   Copia el `token` del `AuthResponse` y presiona **Authorize → Bearer <token>** para autenticación de Bruno.

### 2. Convertir a Bruno en proveedor

1. **POST `/api/iam/providers/onboard`** (sin cuerpo). La respuesta actualiza los roles del usuario autenticado y debe incluir `ROLE_PROVIDER`.

2. **POST `/api/providing/onboarding`** con:

   ```json
   {
     "displayName": "Bruno Provider",
     "phone": "+51 900123456",
     "docType": "DNI",
     "docNumber": "44556677"
   }
   ```
   Se creará un `ProviderResource` con estado `PENDING`.

3. **POST `/api/providing/kyc`** para completar datos:

   ```json
   {
     "displayName": "Bruno Provider SAC",
     "phone": "+51 900123456",
     "docType": "RUC",
     "docNumber": "10445566779"
   }
   ```

   El estado seguirá en `PENDING` hasta que sea aprobado por un admin.

### 3. Aprobar proveedor con el usuario Admin
1. Desautentica a Bruno en Swagger (botón **Logout**) y autentica al admin realizando **POST `/api/iam/auth/login`** con:

   ```json
   { "email": "admin@bikelab.io", "password": "Admin#123" }
   ```

   Usa el token recibido en **Authorize**.

2. **GET `/api/providing`** con query `status=PENDING` para ubicar el `providerId` de Bruno.

3. **POST `/api/providing/{providerId}/approve`** con:

   ```json
   { "reason": "Documentación validada" }
   ```

   El `ProviderResource` debe mostrar `status: "APPROVED"` y una entrada en `verifications`.

4. **POST `/api/providing/{providerId}/reject`** permite validar el flujo de rechazo usando otro proveedor de prueba.

### 4. Gestionar vehículos como Bruno
1. Vuelve a autenticar a Bruno (login + Authorize).

2. **POST `/api/vehicles`** con:

   ```json
   {
     "title": "MTB Trek Fuel EX",
     "description": "Bicicleta trail 29\" con suspensión completa",
     "hourlyPrice": 18.50,
     "latitude": -12.0862,
     "longitude": -76.9774
   }
   ```

   Guarda el `vehicleId` de la respuesta (`VehicleResource`).

3. **PATCH `/api/vehicles/{vehicleId}`** con cambios parciales, por ejemplo:

   ```json
   { "description": "Incluye casco y candado" }
   ```

4. **POST `/api/vehicles/{vehicleId}/availability/block`**:

   ```json
   {
     "startAt": "2024-07-10T09:00:00Z",
     "endAt": "2024-07-10T11:00:00Z"
   }

   ```
   Conserva el `slotId` si necesitas desbloquear la franja usando **POST `/api/vehicles/{vehicleId}/availability/unblock?slotId=<slotId>`**.

5. Explora **GET `/api/vehicles`**, **GET `/api/vehicles/own`** y **GET `/api/vehicles/search`** para verificar lectura pública y privada.

### 4. Registrar a Alice y agregar un método de pago
1. **POST `/api/iam/auth/register`** con los datos de Alice y luego **POST `/api/iam/auth/login`** para obtener su token. Autentícala en Swagger (Authorize).

2. **POST `/api/payments/methods`** con:
   ```json
   {
     "tokenRef": "tok_test_4242",
     "brand": "VISA",
     "last4": "4242",
     "makeDefault": true
   }
   ```

3. **GET `/api/payments/methods`** debe mostrar el método recién creado como `isDefault = true`.

### 5. Crear una reserva como Alice
1. Con Alice autenticada, abre **POST `/api/renting/bookings`** y usa fechas futuras. Ejemplo:

   ```json
   {
     "vehicleId": "22222222-2222-2222-2222-222222222222",
     "startAt": "2024-07-15T15:00:00Z",
     "endAt": "2024-07-15T18:00:00Z"
   }
   ```

2. La respuesta (`201`) incluye `BookingResource` con estado `CONFIRMED`, `authorizedAmount` y `bookingId`. Guarda el identificador para las siguientes acciones y verifica `GET /api/renting/bookings/mine`.

### 6. Cancelar antes de iniciar

**DELETE `/api/renting/bookings/{bookingId}`** con Alice autenticada. Debe retornar `204` y liberar la reserva (observa que el cargo pasa a `FAILED` en la tabla `charges`).

### 7. Operar la reserva como Bruno

1. Con Bruno autenticado, **POST `/api/renting/bookings/{bookingId}/start`**. El estado pasa a `ACTIVE` y se registra `activatedAt`. Swagger mostrará el recurso actualizado.
2. Luego, **POST `/api/renting/bookings/{bookingId}/finish`**. El estado cambia a `FINISHED`, se calcula `penaltyAmount` si aplica y se dispara la captura del cargo.

### 8. Validar operaciones de pagos

1. **POST `/api/payments/charges/authorize`**. Ejemplo:
   ```json
   {
     "bookingId": "<booking-id>",
     "amount": 45.00,
     "currency": "PEN",
     "idempotencyKey": "charge-001"
   }
   ```

2. **POST `/api/payments/charges/{chargeId}/capture`** con cuerpo:

   ```json
   { "idempotencyKey": "capture-001" }
   ```

   Si autenticas al admin, la captura puede hacerse con `overrideOwnership=true` automáticamente.

3. **POST `/api/payments/charges/{chargeId}/refund`** (solo admin o soporte) con:

   ```json
   {
     "amount": 10.00,
     "reason": "Compensación"
   }
   ```

### 9. Consultar payouts del proveedor

Autentica a Bruno y ejecuta **GET `/api/payments/payouts/mine`**. Mientras no existan procesos automáticos de pagos, responderá una lista vacía, lo que permite comprobar el endpoint y la seguridad.

### 10. Consultas IAM útiles
- **GET `/api/iam/me`**: devuelve el `UserResource` del token autenticado.
- **GET `/api/admin/users`**: disponible solo para `ROLE_ADMIN`, muestra la lista completa de usuarios.

### 11. Manejo de errores en Swagger
- Las validaciones muestran `ProblemDetails` o un objeto `{ "message": "<detalle>" }` según el bounded context.
- Los intentos de operar recursos ajenos devuelven `403` con mensajes como `Customer is not owner of booking`.
- Recursos inexistentes responden `404`. Puedes forzar el caso ingresando IDs aleatorios desde Swagger.

### 12. Bounded context **Metrics**
Este nuevo bounded context expone información agregada para tableros en el frontend y no requiere autenticación.

- **GET `/api/metrics/overview`**  
    ```json
    {
      "usersTotal": 18,
      "providersApproved": 5,
      "vehiclesAvailable": 12,
      "vehiclesInService": 2,
      "bookingsConfirmed": 3,
      "bookingsActive": 1,
      "bookingsFinished": 8,
      "paymentsAuthorized": 15,
      "paymentsCaptured": 12
    }
    ```