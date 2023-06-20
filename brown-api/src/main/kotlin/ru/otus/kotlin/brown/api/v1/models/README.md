### Known problems
Due to bug in openApiGenerator discriminator field appears twice after serialization of corresponding response classes:

https://github.com/OpenAPITools/openapi-generator/issues/11347 

The problem can be solved by adding `access` parameter for inherited discriminator field in generated classes like

   ```kotlin
   @field:JsonProperty("requestType", access = JsonProperty.Access.WRITE_ONLY)
   override val requestType: string
   ...
   ```
Gradle task `ApiModelPostProcessor` was written to add this parameter rigth after model generation. In particular, there are two discriminator property `requestType` and `responseType` that are 

These class files along with some unused generated classes are listed in `.ignored` file. They won't be overwritten by OpenApiGenerator unless `ignoreFileOverride` parameter in `build.gradle.kts` points to this ignore file.