package es.sebas1705.models

import androidx.annotation.StringRes
import es.sebas1705.core.resources.R as ResourceR

val Categories.nameRes: Int
    @StringRes get() = when (this) {
        Categories.ANIMALS -> ResourceR.string.core_resources_cat_animals
        Categories.FOOD_AND_DRINKS -> ResourceR.string.core_resources_cat_food_and_drinks
        Categories.SPORTS -> ResourceR.string.core_resources_cat_sports
        Categories.MUSIC -> ResourceR.string.core_resources_cat_music
        Categories.MOVIES_AND_SERIES -> ResourceR.string.core_resources_cat_movies_and_series
        Categories.ANIME -> ResourceR.string.core_resources_cat_anime
        Categories.VIDEO_GAMES -> ResourceR.string.core_resources_cat_video_games
        Categories.CELEBRITIES -> ResourceR.string.core_resources_cat_celebrities
        Categories.SOCIAL_MEDIA_AND_STREAMERS -> ResourceR.string.core_resources_cat_social_media_and_streamers
        Categories.INTERNET_AND_MEMES -> ResourceR.string.core_resources_cat_internet_and_memes
        Categories.BRANDS -> ResourceR.string.core_resources_cat_brands
        Categories.STORES_AND_RETAIL -> ResourceR.string.core_resources_cat_stores_and_retail
        Categories.APPS_AND_PLATFORMS -> ResourceR.string.core_resources_cat_apps_and_platforms
        Categories.CARS_AND_MOTORSPORT -> ResourceR.string.core_resources_cat_cars_and_motorsport
        Categories.CITIES_AND_LANDMARKS -> ResourceR.string.core_resources_cat_cities_and_landmarks
        Categories.ADULT_18_PLUS -> ResourceR.string.core_resources_cat_adult_18_plus
        Categories.TECHNOLOGY -> ResourceR.string.core_resources_cat_technology
        Categories.SCIENCE_PHYSICS -> ResourceR.string.core_resources_cat_science_physics
        Categories.SCIENCE_CHEMISTRY -> ResourceR.string.core_resources_cat_science_chemistry
        Categories.SCIENCE_BIOLOGY -> ResourceR.string.core_resources_cat_science_biology
        Categories.SPACE_AND_ASTRONOMY -> ResourceR.string.core_resources_cat_space_and_astronomy
        Categories.HISTORY -> ResourceR.string.core_resources_cat_history
        Categories.GEOGRAPHY -> ResourceR.string.core_resources_cat_geography
        Categories.TRANSPORT -> ResourceR.string.core_resources_cat_transport
        Categories.PROFESSIONS -> ResourceR.string.core_resources_cat_professions
        Categories.SCHOOL_AND_EDUCATION -> ResourceR.string.core_resources_cat_school_and_education
        Categories.HEALTH_AND_MEDICINE -> ResourceR.string.core_resources_cat_health_and_medicine
        Categories.FASHION_AND_CLOTHING -> ResourceR.string.core_resources_cat_fashion_and_clothing
        Categories.HOME_AND_FURNITURE -> ResourceR.string.core_resources_cat_home_and_furniture
        Categories.NATURE_AND_WEATHER -> ResourceR.string.core_resources_cat_nature_and_weather
        Categories.HOLIDAYS_AND_CELEBRATIONS -> ResourceR.string.core_resources_cat_holidays_and_celebrations
        Categories.MYTHOLOGY_AND_FANTASY -> ResourceR.string.core_resources_cat_mythology_and_fantasy
        Categories.BOOKS_AND_LITERATURE -> ResourceR.string.core_resources_cat_books_and_literature
        Categories.ART_AND_DESIGN -> ResourceR.string.core_resources_cat_art_and_design
        Categories.BUSINESS_AND_FINANCE -> ResourceR.string.core_resources_cat_business_and_finance
        Categories.POLITICS_AND_LAW -> ResourceR.string.core_resources_cat_politics_and_law
        Categories.CULTURE_AND_TRADITIONS -> ResourceR.string.core_resources_cat_culture_and_traditions
        Categories.TRAVEL_AND_TOURISM -> ResourceR.string.core_resources_cat_travel_and_tourism
        Categories.SUPERHEROES_AND_COMICS -> ResourceR.string.core_resources_cat_superheroes_and_comics
        Categories.BOARD_GAMES_AND_TABLETOP -> ResourceR.string.core_resources_cat_board_games_and_tabletop
        Categories.KITCHEN_AND_COOKWARE -> ResourceR.string.core_resources_cat_kitchen_and_cookware
        Categories.OFFICE_AND_STATIONERY -> ResourceR.string.core_resources_cat_office_and_stationery
        Categories.EMOTIONS_AND_FEELINGS -> ResourceR.string.core_resources_cat_emotions_and_feelings
        Categories.VALUES_AND_ETHICS -> ResourceR.string.core_resources_cat_values_and_ethics
    }

val Categories.descriptionRes: Int
    @StringRes get() = when (this) {
        Categories.ANIMALS -> ResourceR.string.core_resources_cat_animals_desc
        Categories.FOOD_AND_DRINKS -> ResourceR.string.core_resources_cat_food_and_drinks_desc
        Categories.SPORTS -> ResourceR.string.core_resources_cat_sports_desc
        Categories.MUSIC -> ResourceR.string.core_resources_cat_music_desc
        Categories.MOVIES_AND_SERIES -> ResourceR.string.core_resources_cat_movies_and_series_desc
        Categories.ANIME -> ResourceR.string.core_resources_cat_anime_desc
        Categories.VIDEO_GAMES -> ResourceR.string.core_resources_cat_video_games_desc
        Categories.CELEBRITIES -> ResourceR.string.core_resources_cat_celebrities_desc
        Categories.SOCIAL_MEDIA_AND_STREAMERS -> ResourceR.string.core_resources_cat_social_media_and_streamers_desc
        Categories.INTERNET_AND_MEMES -> ResourceR.string.core_resources_cat_internet_and_memes_desc
        Categories.BRANDS -> ResourceR.string.core_resources_cat_brands_desc
        Categories.STORES_AND_RETAIL -> ResourceR.string.core_resources_cat_stores_and_retail_desc
        Categories.APPS_AND_PLATFORMS -> ResourceR.string.core_resources_cat_apps_and_platforms_desc
        Categories.CARS_AND_MOTORSPORT -> ResourceR.string.core_resources_cat_cars_and_motorsport_desc
        Categories.CITIES_AND_LANDMARKS -> ResourceR.string.core_resources_cat_cities_and_landmarks_desc
        Categories.ADULT_18_PLUS -> ResourceR.string.core_resources_cat_adult_18_plus_desc
        Categories.TECHNOLOGY -> ResourceR.string.core_resources_cat_technology_desc
        Categories.SCIENCE_PHYSICS -> ResourceR.string.core_resources_cat_science_physics_desc
        Categories.SCIENCE_CHEMISTRY -> ResourceR.string.core_resources_cat_science_chemistry_desc
        Categories.SCIENCE_BIOLOGY -> ResourceR.string.core_resources_cat_science_biology_desc
        Categories.SPACE_AND_ASTRONOMY -> ResourceR.string.core_resources_cat_space_and_astronomy_desc
        Categories.HISTORY -> ResourceR.string.core_resources_cat_history_desc
        Categories.GEOGRAPHY -> ResourceR.string.core_resources_cat_geography_desc
        Categories.TRANSPORT -> ResourceR.string.core_resources_cat_transport_desc
        Categories.PROFESSIONS -> ResourceR.string.core_resources_cat_professions_desc
        Categories.SCHOOL_AND_EDUCATION -> ResourceR.string.core_resources_cat_school_and_education_desc
        Categories.HEALTH_AND_MEDICINE -> ResourceR.string.core_resources_cat_health_and_medicine_desc
        Categories.FASHION_AND_CLOTHING -> ResourceR.string.core_resources_cat_fashion_and_clothing_desc
        Categories.HOME_AND_FURNITURE -> ResourceR.string.core_resources_cat_home_and_furniture_desc
        Categories.NATURE_AND_WEATHER -> ResourceR.string.core_resources_cat_nature_and_weather_desc
        Categories.HOLIDAYS_AND_CELEBRATIONS -> ResourceR.string.core_resources_cat_holidays_and_celebrations_desc
        Categories.MYTHOLOGY_AND_FANTASY -> ResourceR.string.core_resources_cat_mythology_and_fantasy_desc
        Categories.BOOKS_AND_LITERATURE -> ResourceR.string.core_resources_cat_books_and_literature_desc
        Categories.ART_AND_DESIGN -> ResourceR.string.core_resources_cat_art_and_design_desc
        Categories.BUSINESS_AND_FINANCE -> ResourceR.string.core_resources_cat_business_and_finance_desc
        Categories.POLITICS_AND_LAW -> ResourceR.string.core_resources_cat_politics_and_law_desc
        Categories.CULTURE_AND_TRADITIONS -> ResourceR.string.core_resources_cat_culture_and_traditions_desc
        Categories.TRAVEL_AND_TOURISM -> ResourceR.string.core_resources_cat_travel_and_tourism_desc
        Categories.SUPERHEROES_AND_COMICS -> ResourceR.string.core_resources_cat_superheroes_and_comics_desc
        Categories.BOARD_GAMES_AND_TABLETOP -> ResourceR.string.core_resources_cat_board_games_and_tabletop_desc
        Categories.KITCHEN_AND_COOKWARE -> ResourceR.string.core_resources_cat_kitchen_and_cookware_desc
        Categories.OFFICE_AND_STATIONERY -> ResourceR.string.core_resources_cat_office_and_stationery_desc
        Categories.EMOTIONS_AND_FEELINGS -> ResourceR.string.core_resources_cat_emotions_and_feelings_desc
        Categories.VALUES_AND_ETHICS -> ResourceR.string.core_resources_cat_values_and_ethics_desc
    }
