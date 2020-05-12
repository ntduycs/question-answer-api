# Getting Started

## Database 

#### Please create a database with name `halocom_test` to make sure building process successfully.

## How to run

`cd <root_directory>`
`./mvnw package && java -jar target/javaintern-0.1.0-SNAPSHOT.jar`

## Endpoints

All request should have the `Content-Type` header is set to `application/json`.

1. POST: `/api/auth/login`
   ```json
   {
     "email" : "your_email",
     "password": "your_password"
   }
   ```
   
   Returns 200 OK response in case of successful and otherwise, return 401
   
2. POST: `/api/users` - Registers new user
    ```json
    {
     "email": "your_email",
     "password": "your_password",
     "confirmPassword": "confirm_password",
     "fullName": "your_full_name"
    }
    ``` 
   
   Returns 201 CREATED response in case of successful (including `Location` response header pointing to resource location)
   
3. GET: `/api/users/{id}`
    
   Return 200 OK response in case of successful
   
4. POST: `/api/questions` - Creates new question
    ```json
    {
     "content": "question_content",
     "expiration": {
       "days": "num_days_to_expiry",
       "hours": "num_hours_to_expiry"
     },
     "choices": [
       {
         "content": "first_choice_content"
       },
       {
         "content": "second_choice_content"
       }
     ],
     "tags": ["tag_1", "tag_2"],
     "open": "true_or_false"
    }
    ```
   `tags` is optional, the others are required.
   
   `open` indicates the created question should allow the others that are not its owner to add new choice to
   
   Returns 201 CREATED response in case of successful (including `Location` response header pointing to resource location)
   
5. GET: `/api/questions/{id}` - Fetches question that is not expired by id
    
   Returns 200 OK response in case of successful
   
6. GET: `/api/questions` - Listing questions that are not expired

   Allowed request parameters:
   - `page`: int - the offset to the start fetching position
   - `size`: int - the limit fetched items
   - `sortBy`: string - the field should be used to sort items
   - `direction`: string - sort direction, accepts `asc` or `desc`,
   - `tag`: stirng - tag name to filter
   
   Returns 200 OK response in case of successful
   
7. DELETE: `/api/questions/{id}` - Softly delete a single question by id

   Returns 204 NO CONTENT response in case of successful
   
8. POST: `/api/questions/{id}/choices` - Add new choice to the specified question

   ```json
    {
     "content": "choice_content"
    }
   ```
   
   Only allowed in case the question is open and choice's content has been not existing yet.
   
   Returns 201 CREATED response in case of successful (including `Location` response header pointing to resource location)
   
9. POST: `/api/questions/{id}/votes`

   ```json
    {
     "choiceId": "choice_id"
    }
    ```
   
   This endpoint will be on behalf of user, upvote the the specified choice in case he has not voted for this choice,
   and downvote on the other hand.
   
   Not allowed voting for expired or deleted questions (including softly deletions)
   
   Return 200 OK response in case of successful
   
  
   
