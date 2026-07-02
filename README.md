# MusicDiary 

**MusicDiary** is a full-stack music discovery and journaling platform where users can search for artists and tracks, save their favorites, organize their music history with filters, attach personal notes to each saved song, and build a personal music history. The application integrates with the Last.fm API to provide rich music data, and features a secure authentication system with JWT tokens.

This project was developed as a portfolio project following the completion of a 26-week Software Engineering intensive program at Code for All_, consolidating skills in full-stack architecture, REST API integration, and security.

## Known Limitations

- **Artist Images:** The Last.fm API removed artist image support in 2019 due to copyright restrictions. 
  Images are unavailable for artists; placeholder images are shown instead. 

## Tech Stack

- **Backend:** Java 21, Spring Boot, Spring Data JPA, Hibernate, Spring Security, REST APIs
- **Frontend:** HTML5, CSS3, Vanilla JavaScript, Bootstrap 5
- **Authentication:** JWT (JSON Web Tokens) + BCrypt
- **External API:** Last.fm API
- **Database:** PostgreSQL
- **Build Tool:** Maven

## Getting Started

### Prerequisites

- Java JDK 21 installed
- Maven installed
- PostgreSQL (installed and running)
- An IDE (IntelliJ IDEA recommended) or a terminal with Java support
- Last.fm API Key (free at [last.fm/api](https://www.last.fm/api))

### How to Run

1. **Clone the repository:**

```
git clone https://github.com/claudioosilvestre/MusicDiary.git
```

2. **Configure Database:**

```sql
CREATE DATABASE musicdiary_db;
```

3. **Configure application.properties:**

```
Update the following fields in backend/src/main/resources/application.properties:

- DATABASE
spring.datasource.url=jdbc:postgresql://localhost:5432/musicdiary_db
spring.datasource.username=your_username
spring.datasource.password=your_password

- JWT
jwt.secret=your_secret_key_minimum_32_characters
jwt.expiration=7200000

- LAST.FM
lastfm.api.key=your_lastfm_api_key
```

4. **Run the backend:**

```
cd backend
mvn spring-boot:run
```

5. **Open the frontend:**

Open `frontend/index.html` with Live Server or your preferred method.

> Note: All database tables are created automatically on the first run (spring.jpa.hibernate.ddl-auto=update).

## Features

- **User Authentication:** Secure registration and login with JWT tokens and BCrypt password hashing.
- **Profile Management:** View and edit profile details, change password with current-password verification, and self-service account deletion.
- **Artist Search:** Search for artists by name using the Last.fm API with listener counts and images.
- **Track Search:** Search for tracks by name with artist information and listener statistics.
- **Favorites System:** Save artists and tracks to a personal favorites list with timestamps.
- **Music History:** View all saved favorites in a dedicated history page.
- **Advanced Filtering:** Filter your saved songs by title, artist name, or a custom date range to quickly find entries in your music diary.
- **Personal Notes:** Add and edit personal notes to saved favorites, with a modal interface and character limit.
- **Modern UI:** Dark-themed responsive interface built with Bootstrap 5 and custom CSS.

## Technical Highlights

- **Spring Security + JWT:** Stateless authentication with JWT filter intercepting every request, extracting and validating tokens automatically.
- **Clean Architecture:** Strict separation of concerns with Controllers, Services, Repositories, DTOs, and Converters.
- **Last.fm Integration:** WebClient-based integration consuming the Last.fm REST API with JSON parsing via Jackson ObjectMapper.
- **Database Design:** Relational model with User, Song, and SavedSong entities — avoiding data duplication by sharing Song records across users.
- **CORS Configuration:** Global CORS configuration supporting multiple origins for development and production environments.
- **Security Best Practices:** Passwords hashed with BCrypt, JWT secrets stored in environment-specific properties files (excluded from version control).
- **Unit Testing:** Unit tests covering Services with JUnit 5 and Mockito, including authentication, JWT, and music diary operations.

## Future Improvements

- Unit and integration tests for Controller layers
- Migrate artist/album image data to the Spotify Web API to resolve Last.fm's image limitations
- Music statistics and listening insights dashboard
- Playlist creation and management
- OAuth2 login with Spotify
- AI-powered music recommendations based on saved history

## Author

**Cláudio Silvestre** — [LinkedIn](https://www.linkedin.com/in/claudioosilvestre)
