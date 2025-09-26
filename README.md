Movie Management & Media Organizer

A Java-based application to scan, process, and organize movie files from local disks and external sources, built with Spring Boot, Hibernate/JPA, and MySQL. The app automatically extracts technical metadata, organizes movies, and stores them in a database for easy management.

Features

- Scan movie files from local disks and external sources.
- Extract technical metadata using FFmpeg: Audio channels, bitrates, codecs, resolution, and subtitles.
- Store metadata in MySQL database: Movie title, writers, actors, genres, languages, resolution, audio/video codecs, subtitles, and ratings.
- Fetch movie information via REST APIs and automatically rename and categorize files.
- Filter movies by resolution, language, genre, audio quality, and rating using configurable properties.
- Secure access with JWT authentication.
- Data validation with Jakarta Bean Validations.
- Clean code with Lombok.
- Integrated tests to ensure code reliability.

Installation & Setup



Usage

- Drop movie files into the configured directories.
- The app will automatically scan, extract metadata, and store information in the database.
- Filter or search movies based on your preferences (resolution, genres, languages, etc.).
- Optionally, connect external sources and APIs to fetch additional metadata.

Future Improvements

- Add a full-featured frontend for easy browsing of movies.
- Add support to create and manage TV shows, including episodes and seasons.
