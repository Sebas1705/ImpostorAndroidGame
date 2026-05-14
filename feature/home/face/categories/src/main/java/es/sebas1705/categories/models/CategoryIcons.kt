package es.sebas1705.categories.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.Biotech
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Celebration
import androidx.compose.material.icons.outlined.Checkroom
import androidx.compose.material.icons.outlined.Computer
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Extension
import androidx.compose.material.icons.outlined.FlightTakeoff
import androidx.compose.material.icons.outlined.Gavel
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.Landscape
import androidx.compose.material.icons.outlined.LocalDining
import androidx.compose.material.icons.outlined.Memory
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.SelfImprovement
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.SportsEsports
import androidx.compose.material.icons.outlined.SportsSoccer
import androidx.compose.material.icons.outlined.TheaterComedy
import androidx.compose.material.icons.outlined.Train
import androidx.compose.material.icons.outlined.Work
import androidx.compose.ui.graphics.vector.ImageVector
import es.sebas1705.models.Categories

@Suppress("CyclomaticComplexMethod")
internal fun Categories.toCategoryIcon(): ImageVector = when (this) {
    Categories.ANIMALS -> Icons.Outlined.Pets
    Categories.FOOD_AND_DRINKS -> Icons.Outlined.LocalDining
    Categories.SPORTS -> Icons.Outlined.SportsSoccer
    Categories.MUSIC -> Icons.Outlined.TheaterComedy
    Categories.MOVIES_AND_SERIES -> Icons.Outlined.Movie
    Categories.ANIME -> Icons.Outlined.AutoStories
    Categories.VIDEO_GAMES -> Icons.Outlined.SportsEsports
    Categories.CELEBRITIES -> Icons.Outlined.Groups
    Categories.SOCIAL_MEDIA_AND_STREAMERS -> Icons.Outlined.Groups
    Categories.INTERNET_AND_MEMES -> Icons.Outlined.Extension
    Categories.BRANDS -> Icons.Outlined.Description
    Categories.STORES_AND_RETAIL -> Icons.Outlined.ShoppingBag
    Categories.APPS_AND_PLATFORMS -> Icons.Outlined.Computer
    Categories.CARS_AND_MOTORSPORT -> Icons.Outlined.DirectionsCar
    Categories.CITIES_AND_LANDMARKS -> Icons.Outlined.Public
    Categories.ADULT_18_PLUS -> Icons.Outlined.Description
    Categories.TECHNOLOGY -> Icons.Outlined.Computer
    Categories.SCIENCE_PHYSICS -> Icons.Outlined.Memory
    Categories.SCIENCE_CHEMISTRY -> Icons.Outlined.Biotech
    Categories.SCIENCE_BIOLOGY -> Icons.Outlined.HealthAndSafety
    Categories.SPACE_AND_ASTRONOMY -> Icons.Outlined.FlightTakeoff
    Categories.HISTORY -> Icons.AutoMirrored.Outlined.MenuBook
    Categories.GEOGRAPHY -> Icons.Outlined.Public
    Categories.TRANSPORT -> Icons.Outlined.Train
    Categories.PROFESSIONS -> Icons.Outlined.Work
    Categories.SCHOOL_AND_EDUCATION -> Icons.Outlined.School
    Categories.HEALTH_AND_MEDICINE -> Icons.Outlined.HealthAndSafety
    Categories.FASHION_AND_CLOTHING -> Icons.Outlined.Checkroom
    Categories.HOME_AND_FURNITURE -> Icons.Outlined.ShoppingBag
    Categories.NATURE_AND_WEATHER -> Icons.Outlined.Landscape
    Categories.HOLIDAYS_AND_CELEBRATIONS -> Icons.Outlined.Celebration
    Categories.MYTHOLOGY_AND_FANTASY -> Icons.Outlined.AutoStories
    Categories.BOOKS_AND_LITERATURE -> Icons.Outlined.Book
    Categories.ART_AND_DESIGN -> Icons.Outlined.Palette
    Categories.BUSINESS_AND_FINANCE -> Icons.Outlined.AccountBalance
    Categories.POLITICS_AND_LAW -> Icons.Outlined.Gavel
    Categories.CULTURE_AND_TRADITIONS -> Icons.Outlined.Groups
    Categories.TRAVEL_AND_TOURISM -> Icons.Outlined.Explore
    Categories.SUPERHEROES_AND_COMICS -> Icons.Outlined.Extension
    Categories.BOARD_GAMES_AND_TABLETOP -> Icons.Outlined.Memory
    Categories.KITCHEN_AND_COOKWARE -> Icons.Outlined.LocalDining
    Categories.OFFICE_AND_STATIONERY -> Icons.Outlined.Description
    Categories.EMOTIONS_AND_FEELINGS -> Icons.Outlined.SelfImprovement
    Categories.VALUES_AND_ETHICS -> Icons.Outlined.Gavel
}




