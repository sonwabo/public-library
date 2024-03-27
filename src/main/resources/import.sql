-- Insert libraries
INSERT INTO public_library.library (name, location) VALUES ('Germiston Public Library', 'Gauteng East');

-- Insert Books
INSERT INTO public_library.books (title, author, genre, publication_date, library_id) VALUES ('Data Structures & Algorithms Made Easy', 'MR Narasimha Karumanchi', 'ACADEMIA', current_date, 1 );
INSERT INTO public_library.books (title, author, genre, publication_date, library_id) VALUES ('Solo leveling (Manga)', 'Chugong', 'MANGA', current_date + 2, 1);
INSERT INTO public_library.books (title, author, genre, publication_date, library_id) VALUES ('Jujutsu Kaisen', 'Mappa Studio', 'MANGA', current_date + 3, 1);

-- Insert library hours
INSERT INTO public_library.library_hours (day_of_week, opening_hours, closing_hours, library_id) VALUES ('MONDAY', '09:00', '17:00', 1);
INSERT INTO public_library.library_hours (day_of_week, opening_hours, closing_hours, library_id) VALUES ('TUESDAY', '09:00', '17:00', 1);

--NOTES
-- Do not format the script
-- H2 default behaviour executes each line as a new statement regardless of the semicolon to indicate end of statement,
-- so breaking up [insert into()] and [values()] will fail;