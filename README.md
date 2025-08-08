# Digital Wallet API

Bu proje, dijital ödeme şirketleri için müşteri ve çalışanların cüzdan oluşturup, para yatırma ve çekme işlemleri yapabileceği bir backend servistir.

## Özellikler

- Kullanıcı kaydı ve JWT ile login
- Cüzdan oluşturma
- Cüzdan listeleme (sadece kendi cüzdanlarını görme)
- Para yatırma (deposit)
- Para çekme (withdraw)
- İşlem onaylama (approve/deny)
- İşlem listeleme
- Role bazlı API erişim kısıtlaması (EMPLOYEE/CUSTOMER)
- Şifreler BCrypt ile encode edilerek saklanır

## Kurulum

1. **Projeyi klonlayın:**
   ```bash
   git clone <repo-url>
   cd digiwall
   ```
2. **Build edin:**
   ```bash
   ./mvnw clean install
   ```
3. **Uygulamayı başlatın:**
   ```bash
   ./mvnw spring-boot:run
   ```
4. **H2 Console:**
   - [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
   - JDBC URL: `jdbc:h2:mem:digiwalldb`

## API Dokümantasyonu

Swagger arayüzüne erişmek için:
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Authentication & Authorization

- Tüm endpointlere erişim için JWT token gereklidir.
- Login için `/auth/login` endpointi kullanılır.
- Çalışanlar (EMPLOYEE) tüm müşteriler için işlem yapabilir.
- Müşteriler (CUSTOMER) sadece kendi cüzdanları ve işlemleri için API'leri kullanabilir.
- Şifreler veritabanında encode (BCrypt) olarak saklanır.

**Header:**

```
Authorization: Bearer <token>
```

## Örnek Kullanıcılar (Seed Data)

- Çalışan: `employee1` / `password`
- Müşteri: `customer1` / `password`
- Müşteri: `customer2` / `password`

> Not: Varsayılan kullanıcılar ve şifreler için seed işlemi `DigiwallApplication.java` içinde yapılır.

## API Endpointleri

### 1. Login

- **POST** `/auth/login`
- **Body:**
  ```json
  {
    "username": "employee1",
    "password": "password"
  }
  ```
- **Başarılı Yanıt:**
  ```json
  {
    "token": "<jwt-token>",
    "role": "EMPLOYEE",
    "customerId": 1
  }
  ```

### 2. Cüzdan Oluşturma

- **POST** `/wallet/create`
- **Header:**
  ```
  Authorization: Bearer <token>
  ```
- **Body:**
  ```json
  {
    "customerId": 2,
    "walletName": "Alışveriş Cüzdanı",
    "currency": "TRY",
    "activeForShopping": true,
    "activeForWithdraw": true
  }
  ```

### 3. Cüzdan Listeleme

- **GET** `/wallet/list?customerId=2`
- **Header:**
  ```
  Authorization: Bearer <token>
  ```

### 4. Para Yatırma (Deposit)

- **POST** `/wallet/deposit`
- **Header:**
  ```
  Authorization: Bearer <token>
  ```
- **Body:**
  ```json
  {
    "walletId": 1,
    "amount": 500,
    "source": "TR000000000000000000000000"
  }
  ```

### 5. Para Çekme (Withdraw)

- **POST** `/wallet/withdraw`
- **Header:**
  ```
  Authorization: Bearer <token>
  ```
- **Body:**
  ```json
  {
    "walletId": 1,
    "amount": 200,
    "destination": "TR000000000000000000000000"
  }
  ```

### 6. İşlem Listeleme

- **GET** `/transaction/list/{walletId}`
- **Header:**
  ```
  Authorization: Bearer <token>
  ```

### 7. İşlem Onaylama (Sadece EMPLOYEE)

- **PUT** `/transaction/approve`
- **Header:**
  ```
  Authorization: Bearer <token>
  ```
- **Body:**
  ```json
  {
    "transactionId": 1,
    "transactionStatus": "APPROVED"
  }
  ```

## Yetkilendirme ve Güvenlik

- JWT tabanlı authentication kullanılmaktadır.
- Tüm şifreler BCrypt ile encode edilerek saklanır.
- Müşteriler sadece kendi cüzdan ve işlemlerine erişebilir. Başkasının verisine erişmeye çalışırsa 403 Forbidden döner.
- Çalışanlar tüm müşteri ve cüzdanlara erişebilir.

## Testler

- Unit testler `src/test/java` altında yer almaktadır.
- Testleri çalıştırmak için:
  ```bash
  ./mvnw test
  ```

## Notlar

- Varsayılan olarak H2 in-memory veritabanı kullanılır.
- JWT secret, port gibi ayarlar için `src/main/resources/application.yml` dosyasını kontrol ediniz.
- Swagger veya API dokümantasyonu eklenmiştir.

---

Daha fazla bilgi veya sorularınız için proje dosyalarını inceleyebilirsiniz.
