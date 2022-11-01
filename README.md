# datasketches-poc

#TEST RUN - 1
Unique count in file 1: 10000000 (numbers from 0 - 10000000)

Unique count in file 2: 10000000 (numbers from 5000000 - 15000000)

Union unique count estimate: 1.4935959710849773E7

Union unique count lower bound 95% confidence: 1.4474713718397593E7

Union unique count upper bound 95% confidence: 1.5411844562857851E7

Intersection unique count estimate: 5035091.891370566

Intersection unique count lower bound 95% confidence: 4841267.202880455

Intersection unique count upper bound 95% confidence: 5236637.271416772

Intersection accuracy: 99.57%

Intersection accuracy: 99.30%

Time Taken: 12ms


#TEST RUN - 2
Unique count in File 1:10000000( 0 - 10000000 )

Unique count in File 2:10000000( 5000000 - 15000000 )

------------------------------------------------------------
Sketching Test for Random integers

Sketching Test Starts

Union unique count estimate: 1.9622051247473326E7

Intersection unique count estimate: 23130.66410870311


**Time Taken:

Generate Skech:1926ms

Skech:17ms

Total:1943ms**

Sketching Test Ends

------------------------------------------------------------
Sketching Test for Sequential integers

Sketching Test Starts

Union unique count estimate: 1.4874678447978454E7

Intersection unique count estimate: 4977579.103119362

**Time Taken:

Generate Skech:1710ms

Skech:4ms

Total:1714ms**

Sketching Test Ends

------------------------------------------------------------
Sketching Test for random profiles

Sketching Test Starts

Union unique count estimate: 1.9353023997928374E7

Intersection unique count estimate: 0.0

**Time Taken:

Generate Skech:7219ms

Skech:3ms

Total:7222ms**

Sketching Test Ends

------------------------------------------------------------
Sketching Test for  profiles

Sketching Test Starts

Union unique count estimate: 1001.0

Intersection unique count estimate: 1001.0

**Time Taken:

Generate Skech:5004ms

Skech:1ms

Total:5005ms**

Sketching Test Ends
