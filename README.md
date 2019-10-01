# App to find most occurring words in provided wiki page  
## To build
> Below commands to checkout the code, compile and test

```console
git clone url
cd findtopwords
gradlew build
```
## Unit test reports
> Unit tests reports path
>> build\reports\tests\test\index.html

## Test Coverage reports
> Test coverage reports path
>> build\coverage\index.html

## To run the program
> First argument should be page_id, second argument should be top n occurances to be found
> Note that valid arguments are mandatory, if not provided the program will show warnings. Program will not throw any error

```console
gradlew run  --args="21721040 5" 
```

