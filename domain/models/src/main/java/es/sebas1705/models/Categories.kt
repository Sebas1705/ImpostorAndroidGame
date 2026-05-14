package es.sebas1705.models

/**
 * Fixed category catalog for the Impostor word bank.
 *
 * This enum is intentionally static so all clients share the same package taxonomy.
 */
enum class Categories(
    val displayName: String,
    val description: String
) {
    ANIMALS("Animals", "Wildlife, pets, sea creatures and insects."),
    FOOD_AND_DRINKS("Food and Drinks", "Meals, ingredients, desserts and beverages."),
    SPORTS("Sports", "Team sports, individual disciplines and competitions."),
    MUSIC("Music", "Instruments, genres, artists and production terms."),
    MOVIES_AND_SERIES("Movies and Series", "Films, TV shows, characters and franchises."),
    ANIME("Anime", "Series, characters, studios and iconic terms from anime culture."),
    VIDEO_GAMES("Video Games", "Game genres, titles, consoles and gaming culture."),
    CELEBRITIES("Celebrities", "Famous actors, artists, athletes and public figures."),
    SOCIAL_MEDIA_AND_STREAMERS("Social Media and Streamers", "Creators, platforms, trends and live content."),
    INTERNET_AND_MEMES("Internet and Memes", "Viral formats, internet slang and meme culture."),
    BRANDS("Brands", "Commercial brands from technology, fashion, food, sports and lifestyle."),
    STORES_AND_RETAIL("Stores and Retail", "Shops, supermarkets, marketplaces and retail concepts."),
    APPS_AND_PLATFORMS("Apps and Platforms", "Popular apps, digital services and online platforms."),
    CARS_AND_MOTORSPORT("Cars and Motorsport", "Car brands, racing terms and motorsport culture."),
    CITIES_AND_LANDMARKS("Cities and Landmarks", "Famous cities, monuments and iconic places."),
    ADULT_18_PLUS("+18", "Adult-themed words intended for mature players only."),
    TECHNOLOGY("Technology", "Devices, software, internet and computing concepts."),
    SCIENCE_PHYSICS("Science: Physics", "Forces, energy, motion and physical laws."),
    SCIENCE_CHEMISTRY("Science: Chemistry", "Elements, reactions, compounds and lab concepts."),
    SCIENCE_BIOLOGY("Science: Biology", "Living organisms, cells, anatomy and ecosystems."),
    SPACE_AND_ASTRONOMY("Space and Astronomy", "Planets, stars, missions and celestial objects."),
    HISTORY("History", "Civilizations, events, eras and historical figures."),
    GEOGRAPHY("Geography", "Countries, capitals, landmarks and natural regions."),
    TRANSPORT("Transport", "Cars, trains, aircraft, ships and mobility systems."),
    PROFESSIONS("Professions", "Jobs, trades and workplace roles."),
    SCHOOL_AND_EDUCATION("School and Education", "Subjects, classroom objects and institutions."),
    HEALTH_AND_MEDICINE("Health and Medicine", "Body parts, symptoms, treatments and healthcare."),
    FASHION_AND_CLOTHING("Fashion and Clothing", "Garments, accessories, styles and trends."),
    HOME_AND_FURNITURE("Home and Furniture", "Rooms, appliances, furniture and household items."),
    NATURE_AND_WEATHER("Nature and Weather", "Landscapes, plants, climate and meteorology."),
    HOLIDAYS_AND_CELEBRATIONS("Holidays and Celebrations", "Festivities, traditions and special dates."),
    MYTHOLOGY_AND_FANTASY("Mythology and Fantasy", "Myths, fantasy creatures and magical lore."),
    BOOKS_AND_LITERATURE("Books and Literature", "Genres, authors, characters and literary terms."),
    ART_AND_DESIGN("Art and Design", "Painting, sculpture, architecture and design concepts."),
    BUSINESS_AND_FINANCE("Business and Finance", "Economy, commerce, money and entrepreneurship."),
    POLITICS_AND_LAW("Politics and Law", "Government, rights, institutions and legal terms."),
    CULTURE_AND_TRADITIONS("Culture and Traditions", "Customs, symbols and regional cultural practices."),
    TRAVEL_AND_TOURISM("Travel and Tourism", "Destinations, lodging, planning and travel items."),
    SUPERHEROES_AND_COMICS("Superheroes and Comics", "Heroes, villains, universes and comic culture."),
    BOARD_GAMES_AND_TABLETOP("Board Games and Tabletop", "Board games, card games and tabletop terms."),
    KITCHEN_AND_COOKWARE("Kitchen and Cookware", "Utensils, cookware and kitchen tools used for cooking."),
    OFFICE_AND_STATIONERY("Office and Stationery", "Desk tools, stationery and workplace supplies."),
    EMOTIONS_AND_FEELINGS("Emotions and Feelings", "Emotional states, moods and inner reactions."),
    VALUES_AND_ETHICS("Values and Ethics", "Principles, morals and social values.")
}

