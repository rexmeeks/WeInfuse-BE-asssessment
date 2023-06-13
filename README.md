# Summary

This bowling score calculator operates takes a list of individual frame scores and returns the sum of each frame into an ordered 
calculated scores list. Using only 0-9, /, and X

Request:
```
{
    "individualFrameScores": [
        ["X", "X", "X", "X", "X", "X", "X", "X", "X", "X"],
        [1, "/", 2, 7, 5, "/", 0, "/", "X", 1, 5, 7, 1, "X", "X", 9, 0, "X"]
    ]
}
```

Response:
```
{
  "calculatedScores": [
    [30, 30, 30, 30, 30, 30, 30, 30, null],
    [12, 9, 10, 20, 16, 6, 8, 29, 19, 19]],
  "errors": null
}
```

* (Errors will be an ordered list of errors for each individual player, like below in the case of player 3 being the only
  correctly represented game, so it's null)
    ```
    {
        "calculatedScores": [],
        "errors": [
            "Frame scores may only be 0-9, /, and X",
            "Frame scores may only be 0-9, /, and X",
            null,
            "Frame scores may only be 0-9, /, and X"
        ]
    }  
    ```

This calculator operates under the assumption that games are at max 10-12 frames, with a max of 21 throws.
Just like bowling, a strike takes the sum of the next two throws and a spare takes the sum of the next throw.
A strike is unable to be calculated without the next two throws (unless it's the end of the game,) so it will display as null (you can see the 
Same with a spare, if the next throw isn't there, that frame will be unable to be calculated and will return as null


# Running the application
This application was made in Java 20, althought I think I'm only using Java 8 features, may just want to have the JDK20.
1. Clone the application
2. From the main directory run:
    * `./mvnw install spring-boot:run`
3. Now that the application is spun up you can hit the endpoint from:
    * `localhost:8080/bowling/calculateScores`
      * You can use the request above as a sample, should be able to pass in any number of player scores

# Running the tests
* From the main directory run:
    * `./mvnw test`
