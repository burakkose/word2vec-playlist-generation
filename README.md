# Playlist2Vec : Playlist Generation with Word2vec
Distributed playlist generation system on Apache Spark and MLlib.

## Dataset
The datasets were collected by Shuo Chen(shuochen@cs.cornell.edu) from Dept. of Computer Science, Cornell University. In this repository, I used their 'yes_complete' dataset. For more details, please look at http://www.cs.cornell.edu/~shuochen/lme/data_page.html

                            | yes_complete
--------------------------- | ------------
Appearance Threshold        | 0
Number of Songs             | 75,262
Number of Train Transitions | 1,542,372
Number of Test Transitions  | 1,298,181

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
(63680,6.775494490858625) -> We Got Something - Tinted Windows
(71334,6.765123370044001) -> Robert Randolph & the Family Band - Jesus Is Just Alright
(10008,6.724309275566067) -> Jimmy Buffett - Son Of A Son Of A Sailor
(65267,6.683474432535816) -> Kansas - Dust in the Wind
(17652,6.680945247885326) -> Bob Dylan - Rainy Day Women #12 & 35
```
