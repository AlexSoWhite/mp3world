# State machines
This app has a lot of screens that follow state machine behaviour - there are initializing, loading, error, empty states etc.
So this package aims to provide common logic for managing these states and providing API to react to state changes.

Key entities:
1. `State` - sealed class that holds states themself:
    - `Initializing` - for describing initial state;
    - `Loading` - for describing loading state;
    - `Error` - for describing error state;
    - `Success` - for describing success state after first loading;
    - `Updated` - for describing success state after following loadings (reloadings);
2. `StateModel` - state machine, that holds `State`. This class provides access to current `State` (initially `Initializing`) and provides API to change it based on current state.
    - `load` - changes current state to `Loading`, only works when current state is `Initializing`;
    - `success` - changes current state to `Success` if current state is `Loading`, otherwise changes it to `Updated`. This difference is for cases when new `Success` state came from some background activity.
    - `error` - changes current state to `Error`, only works when current state is `Loading`;
    - `refresh` - changes current state to `Loading`, only works when current state is not `Loading`;
    - `update` - changes current state to `Updated`, only works when current state is not `Loading`. Designed for updates triggered by background activity.
3. `LState` and `ListStateModel` - extensions of `State` and `StateModel` respectively designed to manage states of lists. `Empty` state is added.
4. At presentation layer firstly we have `StatedViewModel` and `StatedFragment`. `StatedViewmodel` just provides `State` based on submitted `Data` (can be `Success` or `Error`) source (kotlin Flow). `StatedViewModel` collects submitted `Flow<Data>` and responds to emits to this flow by changing `State` accordingly. `StatedFragment` collects current state provided by `StatedViewModel` and calls state render methods (they are abstract) according to each state.
5. For lists there are `StatedListViewModel` and `StatedListFragment`. ViewModel additionally checks that `Data` is empty list and moves current state to `LState.Empty` if needed. `StatedListFragment` submits data to recycler's adapter as well. `StatedListFragmentBaseLayout` is a default implementation for simple list fragment that manages every state. 
6. For playlists the default implementation goes further, making `StatedPlaylistFragmentBaseLayout` and `StatedPlaylistViewModel` to be able to interact with a player and render songs state.