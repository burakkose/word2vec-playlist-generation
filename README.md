# Playlist2Vec : Playlist Generation with Word2vec
Distributed playlist generation system on Apache Spark.

## Dataset
The datasets were collected by Shuo Chen(shuochen@cs.cornell.edu) from Dept. of Computer Science, Cornell University. In this repository, I used their 'yes_complete' dataset. For more details, please look at http://www.cs.cornell.edu/~shuochen/lme/data_page.html

|                            | yes_complete
|--------------------------- | ------------
|Appearance Threshold        | 0
|Number of Songs             | 75,262
|Number of Train Transitions | 1,542,372
|Number of Test Transitions  | 1,298,181

## Results

#### Seed songs (song_id, song_name)
```
3019  -> Simple Man	- Lynyrd Skynyrd
50248 -> Tuesday&s Gone	- Lynyrd Skynyrd
9526  -> Bad Moon Rising - Creedence Clearwater Revival
2708  -> The House Of The Rising Sun - The Animals
```
#### Best five(N = 5) recommendations (song_id, score)
```
(39322,0.9067619076741221) -> Out In The Street	- Bruce Springsteen
(17652,0.881439257058365) -> Rainy Day Women #12 & 35 - Bob Dylan
(54142,0.8686043966007563) -> Walkin' 'Round In Women's Underwear - Bob Rivers
(65267,0.8673158441034937) -> Kansas - Dust in the Wind
(3049,0.8647980810466018) -> Hello, I Love You - The Doors
```