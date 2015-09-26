# LazyLoadListener
Implementation of AbsListView.OnScrollListener which generates event when last item of list is reached. Will help to work with common pagination approach with limit and offset

## Common pagination
It's common aproach to use *limit* and *offset* to implement pagination

*offset* - offset of the first record returned
*limit*	- the number of records returned

## Usage

### 1. Instantiate LazyLoadListener
LazyLoadListener has two constructors:

`LazyLoadOnScrollListener(int bunchSize)` - `bunchSize` is *limit*

`LazyLoadOnScrollListener(int bunchSize, int startLoadOffset)` - limit and offset from bottom, for example if startLoadOffset = 5 and your list contains 20 items, onLoad event will be colled when you reached 15th item. 

`LazyLoadOnScrollListener` is abstract class, you have to implement one method - `onLoad(int offset)`.

don't forget to apply this listener to your `ListView`: 

`listView.setOnScrollListener(new LazyLoadOnScrollListener(10) { ... });`

### 2. onLoad() callback
This callback will be called when you reach ond of list (or itemsCount - startLoadOffset items), `offset` param is count of loaded items and it will play *offset* role. In this method you should do your server call with new *offset* value

### 3. After server call
Notify listener about result of your server call:

call `notifyFail()` - if your server call was unsuccesfull

call `notifyFinish(int lastBunchSize)` - if your server call was succesfull, `lastBunchSize` - number of items that server returned in last server call.

### 4. Additional
You can reset listener by using `reset()`.
You can get count of already loaded items `getLoadedCount()`. 

