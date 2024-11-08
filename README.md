# Overview

This library is built to help construct dynamic searches using Spring Data JPA Specification.
The basic usage scenario involves building complex queries by creating a list of unified objects representing
conditions.

### Dependencies

The following versions of dependencies are used in this lib:

- **Spring Boot**: `2.4.3`
- **JDK**: `11`
- **Kotlin**: `1.7.22`

## 1. Quick start

Assuming we want to perform a search for the following entity and its corresponding repository:

```java

@Entity
public class Customer {
    @Id
    private Long id;
    private String name;
    private LocalDate dateOfBirth;

    //getters, setters, ...
}

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
```

### 1.1 We will need a SpecificationBuilder instance, which can be reused for any project entity:

Java:

```java
import com.outsoft.jss.SpecificationBuilder;

// Reusable, the best approach is to create it as a Spring Bean and reuse it wherever needed.
SpecificationBuilder builder = new SpecificationBuilder(); 
```

Kotlin:

```kotlin
import com.outsoft.jss.SpecificationBuilder

val builder = SpecificationBuilder()
```

### 1.2 Now we can create a list of com.outsoft.jss.SearchCriteria and build a Specification using the SpecificationBuilder instance:

Java:

```java
//Create list of search criteria
List<SearchCriteria> search = new ArrayList<>();
search.add(new SearchCriteria("name", Operation.EQUALITY, "Max",Type.AND));
search.add(new SearchCriteria("dateOfBirth", Operation.GREATER_THAN, "2000-03-22",Type.AND));
//Build specification from criteria list
Specification<Customer> specification = builder.build(search);
//Perform the search using the built specification, which can be used with any Specification<T> compatible method.
List<Customer> result = customerRepository.findAll(specification);
```

Kotlin:

```kotlin
//Create list of search criteria
val search = listOf(
    SearchCriteria("name", SearchCriteria.Operation.EQUALITY, "Max"),
    SearchCriteria("dateOfBirth", SearchCriteria.Operation.GREATER_THAN, "2020-03-22"),
)
//Build specification from criteria list
val specification = builder.build<Customer>(search)
//Perform the search using the built specification, which can be used with any Specification<T> compatible method.
val result: List<Customer> = customerRepository.findAll(specification)
```

In the example above we are performing search for all customers with the name "Max", born after "2020-03-22":

```sql
select * from customer where name = 'MAX'and dateOfBirth >= '2020-03-22'
```

## 2. Customization

### 2.1 Supported types:

By default, SpecificationBuilder is able to handle the following primitives and types(reference to
com.outsoft.jss.TypeConverter):

1. integer, long, boolean, double primitives and all corresponding wrapper types
2. java.time types - LocalDate, LocalDateTime, ZoneOffset, ZonedDateTime
3. java.util.UUID
4. all predefined and custom Enums

To handle other types, we need to implement a custom com.outsoft.jss.TypeConverter and pass it to the
constructor when creating the SpecificationBuilder:

Java:

```java
public class CustomTypeConverter extends TypeConverter {
    @Override
    public @NotNull <T> Object convert(@NotNull String value, @NotNull Class<T> type) {
        if (type == CustomClass.class) {
            return CustomClass.getInstance(value.toUpperCase());
        } else {
            return super.convert(value, type);
        }
    }
}
```

```java
SpecificationBuilder builder = new SpecificationBuilder(new CustomTypeConverter()); 
```

Kotlin:

```kotlin
class CustomTypeConverter : TypeConverter() {
    override fun <T> convert(value: String, type: Class<T>): Any =
        when (type) {
            CustomClass::class.java -> CustomClass.getInstance(value.uppercase())
            else -> super.convert(value, type)
        }
}
```

```kotlin
val builder = SpecificationBuilder(CustomTypeConverter())
```

### 2.1 Custom query building:

By default, the query is built based on the SearchCriteria type: AND, OR, SECURITY:

```aiignore
select * where 
    (all Type.AND criteria using AND condition) 
  AND 
    (all Type.OR criteria using OR condition) 
  AND 
    (all Type.SECURITY criteria using OR condition)
```

In case, any custom logic is needed custom implementation of SpecificationBuilder class can be implemented as well as
SearchCriteria class.
See com.outsoft.jss.SpecificationBuilder as an example.