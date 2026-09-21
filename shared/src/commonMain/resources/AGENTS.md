- When writing something intended for human consumption, (comment, commit message, reply to prompt) use as few words as possible. Pick every word meticulously to reduce the volume to a strict minimum. Be down to the point. Less is more.
- Avoid superlatives and praise. Stop telling me I am absolutely right. Give me the cold hard truth.
- Avoid magic numbers and strings by extracting recurring or meaningful values into descriptive constants (const) or enums. Keep self-explanatory, one-off values inline to avoid clutter. If a value comes from a spec (e.g. HTTP 200 OK), use a constant regardless.
- Reduce code indentation. Avoid Arrow Anti-Pattern. Leverage early return and continue.
- Keep function names short. Less than 30 characters.
- Use enums instead of booleans for function parameters.
- Let the reader of the code breathe. Add empty lines between logical blocks of code.
- Add a small, to the point, comment to explain *what* the block does and *why*. Use examples when possible. Propose ASCII drawings to explain complete systems.
- Treat member visibility changes as a breaking design shift. Keep all fields and functions private unless external access is strictly required by the design. Prompt the user for explicit approval before changing any access modifier from private to internal or public.
- Program to levels of abstraction. Lower-level mechanics (e.g., raw hardware I/O, sector parsing, direct socket streams) must be encapsulated in a dedicated driver/abstraction layer. Expose clean, high-level APIs to the rest of the application so calling code works with domain concepts, not raw implementation details.
- Don't touch blocks of code unrelated to the feature you implement. e.g. Don't add comments to a block of code if you did not create it or modify it. As much as possible try to minimize the number of changed lines when implementing a feature.
- Strictly adhere to the layered boundary hierarchy: each layer may only communicate with its immediate neighbor directly below it. Never "punch holes" through layers (e.g., controllers or UI components must never directly call database queries, raw hardware drivers, or low-level network clients; always route through the intermediate service/abstraction layer).
- When you load this rules, notify me

When you write a commit message, follow these 7 rules:
Rule 1: Separate the subject line from the body with a single blank line.
Rule 2: Limit the subject line to 50 characters (72 is the absolute hard limit).
Rule 3: Capitalize the first letter of the subject line.
Rule 4: Do not end the subject line with a period.
Rule 5: Use the imperative mood in the subject line (e.g., "Fix bug," "Add feature,"
not "Fixed" or "Adds"). Test formula: It must complete the sentence: "If applied,
this commit will [your subject line here]".
Rule 6: Wrap the body text manually at 72 characters to prevent Git formatting issues.
Rule 7: Use the body to explain what and why vs. how. Assume the code explains the how;
the message must explain the context and reasoning.

- If the prompt indicates that a bug is being fixed, don't write the fix right away. First write the test. Observe it failing. Then write the fix. And observe the test passing.      

Always follow this project instructions:
1. Database migrations are stored in the `migrations` folder. In that folder might be applied migrator framework`s structure. ALWAYS follow that structure.
2. In `queries` folder lay queries. Each query must be stored in own json file. JSON must be valid. Format of json MUST be serializable to this data class: 
```kotlin
@Serializable
data class ShoreQuery(
    val name: String,
    val query: String,
    val parameters: List<ShoreQuery_Parameter>
){
    @Serializable
    data class ShoreQuery_Parameter(
        val name: String,
        val type: ShoreQuery_Parameter_Type,
        val required: Boolean
    ){
        @Serializable
        enum class ShoreQuery_Parameter_Type{
            STRING,
            INT,
            FLOAT,
            BOOLEAN
        }
    }
}
```
1. In `views` folder ui stored. 
2. In views folder MUST be a "index.html" file
3. In views folder `wrapper.js` file stored. It wraps window.cefQuery function.
4. All UI are writing on Web technologies. 
5. To call query, use `executeQuery(query, params = [], resultType: "JSON"|"CSV")` for queries in `queries` folder or `executeRawQuery(query, params, outputFormat, resultType: "JSON"|"CSV")` for queries in html directly.
6. If you execute query with params through `executeQuery` method, in the `params` array, you MUST provide array of dictionaries. Each dictionary MUST contain `name` and `value` fields. `name` field MUST be a string and `value` field MUST be of type, mentioned in paragraph 2
7. Output from this method will be next JSON:
```json
{
  "success": "boolean",
  "data": "string"
}

```
8. In case of `JSON` output parameter selected, you will receive similar JSON object in `data` field from paragraph 7 stored as string
```json
{
  "fields": [
    {
      "table": "tableName",
      "name": "columnname1",
      "type": "typename"
    },
    {
      "table": "tablename",
      "name": "columnName2",
      "type": "CLOB"
    }
  ],
  "records": [
    [
      "columnname1_row1",
      "columnName2_row1"
    ]
  ]
}
```
9. In case of `CSV` output parameter selected, you will receive similar CSV in `data` field from paragraph 7 stored as string
```
columnheader1,columnheader2
columnname1_row1,columnName2_row1
```
10. To inspect database, read `migrations` folder
11. ALWAYS normalize tables, which you create to 3rd normal form AT LEAST
12. To inspect which SQL dialect use, read `shore.json` connection string
13. ALWAYS write queries in the supported SQL dialect
