package es.sebas1705.couchbase

import es.sebas1705.couchbase.documents.abstracts.Document
import es.sebas1705.couchbase.documents.abstracts.fromMap
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.reflections.Reflections
import java.lang.reflect.Modifier
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor

class DocumentMappersTests {

    private val reflections = Reflections("es.sebas1705.couchbase")
    private val documentClasses = reflections.getSubTypesOf(Document::class.java)
        .filter { !it.isInterface && !it.isAnonymousClass && !Modifier.isAbstract(it.modifiers) }

    private fun dummyValueFor(param: KParameter, kClass: KClass<*>): Any? = when (val type = param.type.classifier) {
        Int::class -> kotlin.random.Random.nextInt()
        Long::class -> kotlin.random.Random.nextLong()
        String::class -> "random_${kotlin.random.Random.nextInt(1000, 9999)}"
        Boolean::class -> kotlin.random.Random.nextBoolean()
        Double::class -> kotlin.random.Random.nextDouble()
        Float::class -> kotlin.random.Random.nextFloat()
        List::class -> List(kotlin.random.Random.nextInt(1, 4)) { "item_${kotlin.random.Random.nextInt(100)}" }
        else -> {
            // If the type is a subclass of Document, create a dummy instance
            if (type is KClass<*> && type.isSubclassOf(Document::class)) {
                val ctor = type.primaryConstructor
                if (ctor != null) {
                    val args = ctor.parameters.associateWith { dummyValueFor(it, type) }
                    ctor.callBy(args)
                } else null
            } else null
        }
    }

    @Test
    fun `Document to Map and Map to Document conversion for all Document subclasses`() {
        for (clazz in documentClasses) {
            val kClass = clazz.kotlin
            // Logging the class currently being tested
            println("Testing class: ${clazz.simpleName}")
            val ctor = kClass.primaryConstructor ?: continue
            // Preparing dummy arguments for the constructor
            val args = ctor.parameters.associateWith { dummyValueFor(it, kClass) }
            println("Instantiating with arguments: $args")
            // Creating an instance of the class with dummy values
            val instance = ctor.callBy(args)
            println("Instance created: $instance")
            // Converting the instance to a map representation
            val map = (instance as Document).asMap()
            println("Converted to map: $map")
            // Reconstructing the instance from the map
            val converted = map.fromMap(kClass)
            println("Converted from map: $converted")
            // Comparing each property between the original and reconstructed instances
            kClass.members.filterIsInstance<kotlin.reflect.KProperty1<Any, *>>().forEach { prop ->
                val original = prop.get(instance)
                val nuevo = prop.get(converted)
                println("Comparing property ${prop.name}: original=$original, converted=$nuevo")
                assertEquals(
                    "Property ${prop.name} in ${clazz.simpleName}",
                    original,
                    nuevo
                )
            }
            // Logging the completion of the test for the current class
            println("Test completed for ${clazz.simpleName}\n")
        }
        // Logging the successful execution of all tests
        println("All tests executed successfully.")
    }
}