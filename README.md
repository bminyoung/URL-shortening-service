# 0. Contents
1. API design      
1-1. HTTP method    
1-2. System    
2. Overall    
3. UML    
3-1. Use Case    
3-2. Class Diagram    
3-3. Sequence Diagram    
4. About data    
4-1. DB Schema    
4-2. Data flow diagram
5. Understanding Spring
6. Block / Non Block & Async & Sync
7. What I want to do



# 1. API design
## 1-1. HTTP method
|URI|Method|URL Params|Data Params|Success Response|Error Response|
|---|---|---|---|---|---|
|/urls|POST|None|Required:<br><pre>{<br>"originalUrl" : String<br>}</pre>|Code: 200<br>Content: <pre>{<br>"shortUrl" : String<br>}</pre>|Code: 400<br>Content: {error : Bad Request}<br>or<br>Code: 401<br>Content: {error : UNAUTHORIZED}|
|/urls/:shortUrl|GET|Required:<br><pre>{<br>"shortUrl" : String<br>}</pre>|None|Code: 200<br>Content: <pre>{<br>"originalUrl" : String<br>}</pre>|Code: 404<br>Content: {error : NOT FOUND}|
|/urls/:shortUrl|DELETE|Required:<br><pre>{<br>"shortUrl" : String<br>}</pre>|None|Code: 200|Code: 401<br>Content: {error : UNAUTHORIZED}<br>or<br>Code: 404<br>Content: {error : NOT FOUND}|
|/:shortUrl|GET|Required:<br><pre>{<br>"shortUrl" : String<br>}</pre>|None|Code: 200<br>Content: <pre>{<br>"originalUrl" : String<br>}</pre>|Code: 404<br>Content: {error : NOT FOUND}|

## 1-2. System
```

/**
 * Result code of request
 */
enum ResultCode {
	 SUCCESS,
	 STORAGE_FAIL,
	 DUPLICATED,
	 NOT_FOUND,
	 OTHER_ERRORS,
}
```

```
/**
 * Url information
 */
class Url{
	String originalUrl;
	String shortUrl;
	Date registerDate;
}
```

```
/**
 * Add a new url into DB
 *
 * @param url(Url): Url object including already shortened url for register
 * @return Result code of register(success or error code)
 */
public ResultCode registerUrl(Url url);
```

```
/**
 * Get a original url from short url
 *
 * @param shortUrl(String): Short url for search
 * @return Url information including a original url for success, otherwise it returns an error code
 */
public Url getUrl(String shortUrl);
```

```
/**
 * Delete a short url information
 *
 * @param shortUrl(String): Shortened url for delete
 * @return Result code of delete(success or error code)
 */
public ResultCode deleteUrl(String shortUrl);
```

# 2. Overall
![OverAll](https://user-images.githubusercontent.com/23355516/84914289-f932b400-b0aa-11ea-96e5-db0d16ec64bf.png)

# 3. UML
## 3-1. Use Case
![Use Case](https://user-images.githubusercontent.com/23355516/84914268-f33cd300-b0aa-11ea-80b7-408e7f812794.png)
## 3-2. Class Diagram
![Class Diagram](https://user-images.githubusercontent.com/23355516/87226583-46413780-c384-11ea-9ffe-067b33bcb92a.png)
## 3-3. Sequence Diagram
- Register URL   
![Sequence_register](https://user-images.githubusercontent.com/23355516/87227662-1dbd3b80-c38c-11ea-8b3e-78ea06a14af5.png)
- Redirect URL   
![Sequence_redirect](https://user-images.githubusercontent.com/23355516/87227668-24e44980-c38c-11ea-804f-b215f01a3cac.png)
- Get URL (Short<->Original)   
![Sequence_convert](https://user-images.githubusercontent.com/23355516/87227675-2c0b5780-c38c-11ea-90b7-432bc182b087.png)
- Delete URL    
![Sequence_delete](https://user-images.githubusercontent.com/23355516/87227681-34fc2900-c38c-11ea-84d3-1d424783c27e.png)

# 4. About data
## 4-1. DB Schema
[URLINFO]
|Seq|Name|Type|Size|NULL|PK|Desc|
|---|---|---|---|---|---|---|
|1|index|INT||FALSE|PK|Index of Row|
|2|shortUrl|VARCHAR|10|FALSE||Use : /{shortUrl}|
|3|originalUrl|VARCHAR|MAX|FALSE||Full address of original address|
|4|registerDate|DATE||FALSE||date of insertion|

> Which data will put into cache? (not yet)

## 4-2. Data flow diagram
![DFD](https://user-images.githubusercontent.com/23355516/87225581-b8fae480-c37d-11ea-862f-9a61c696ed4e.png)

# 5. Understanding Spring
### 1) Bean
> `Java Object created, assembled, managed by Spring Container`
- Its life cycle is same as Spring Container

### 2) IoC
> `Inversion of Control`
- Control beans from creation to destruction instead of programmer


### 3) DI
> `Dependency Injection`
- Externally inject object of class that depends on other class
- In example, A_1, A_2, A_3 class depend on String class

- Not DI
```
class A_1{
	String depend = "internal def";
}
```

- DI : 1) Create object outside the class 2) Inject
```
// DI through contructor
class A_2{
	String depend;
	
	A_2(String depend){
		this.depend = depend;
	}
}

// DI through setter
class A_3{
	String depend;
	
	public void setDepend(String depend) {
		this.depend = depend;
	}
}
```

- In Spring, there is no need to create object outside. Spring inject bean from container automatically

### 4) Spring operation
![Spring MVC](https://user-images.githubusercontent.com/23355516/84587378-a4e4c580-ae59-11ea-9487-01533e862ac4.png)
1. Request
2. Find Controller that match request
3. Request method to run
4. Execute
5. Return result (using ModelAndView object)
6. Return result
7. Find View
8. Request to response


# 6. Block / Non-Block & Async & Sync
- Block / Non-Block    
 `- Can caller do another work during process of callee?`    
 ![Blocking](https://user-images.githubusercontent.com/23355516/84588076-50444900-ae5f-11ea-84f7-6757737358b5.png)
![Non-Blocking](https://user-images.githubusercontent.com/23355516/84588079-54706680-ae5f-11ea-93a4-e32f0eea7155.png)    

- Async & Sync    
 `- Do caller have to check whether process of callee is end?`    
![Async](https://user-images.githubusercontent.com/23355516/84588152-da8cad00-ae5f-11ea-82d7-fa9cf9cd4329.png)
![Sync](https://user-images.githubusercontent.com/23355516/84588086-5cc8a180-ae5f-11ea-9754-dc9d4d01ea7a.png)

# 7. What I want to do
- add user
- add cache
- converting algorithm
- test code
- (if possible) FE
