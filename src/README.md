--------------------------------------------------HW7-----------------------------------------------------------------
1. Overview
   The goal of our codebase is to allow games of reversi that follow the rules of Regular Reversi
   to be played between two players. There is no pre-requisite knowledge required beyond
   understanding how to make moves and how to play games of Reversi. Our code is intended for
   extensibility, so that new features, like configurable hotkeys personalized for each player
   can be easily added without changing our code very much. Our components (view, controller,
   and model) are decoupled from implementations as much as model and are dependent on interfaces,
   so any model, view, or controller, or combination thereof, that sticks with our interface
   should be able to work with our game of reversi. This means that our code is designed to support
   future new ways of viewing games of reversi and new ways (for example new rules, new start
   positions, new board sizes, new ways to determine things like score and isGameOver, etc.) of
   modeling games of Reversi.

   Some assumptions that we make in our codebase:
   -Players can be either human players (that will make their move by interacting with our view,
   via command line arguments, GUI buttons, or other mechanisms), or AI Players (that will use
   some form of strategy to select their moves)

2. Quick Start
   User can choose the type of players in the reversi game by themselves by entering two additional strings
   after type the java -jar NameOfJARFile.jar. ex. java -jar NameOfJARFile.jar human human means this game
   will be played by two human players, there are four types of ai players offered, easy, medium, mediumplus
   and hard.
   Player can select/deselect the cell by CLICKING the hexagon they want to select, and if this is in their
   turn, the message"it's your turn" will be showed in the north panel net to the current turn
   they can choose to press ENTER to place a cell in the position they selected or press SPACE
   to pass the turn. If the move is invalid, the player will receive a message to notify them, and if
   in this turn, there is no valid move for the current user, a warning will be showed in the north panel
   next to the current turn that this player can only pass, under this circumstance, if the player still wants
   to place a move, that player will be notified that can only choose to pass. Once one of the player take an
   action, the up to dated states of game will be updated to the north panel include the current score for
   both player and the curren turn and the board will be repaint to show the most recent states for the model.
   If the game is over, both of the players will be notified that game is over with the winner of this game.

A user may get started using our controller codebase by interacting with one of our view interface
implementations. Once the game is begun, players will be able to interact with our GUI
by selecting a tile for which to place their color. When a tile is selected, its display
color will become Cyan, indicating  that this is their selection. Tiles can be unselected and selected
by clicking out of bounds,or by clicking to other tiles. When a user wants to make move, they can click
the enter key when they have a selected tile. If the move is valid, it will go through and the corresponding
internal game logic and external view of the game will be updated, with the selected tile
being changed to the color of the player who just made the move. If the move is invalid, they
will know this because the actual color of the selected tile will not be changed to the color
of the player whose turn it is. What's more is there will be a message to notify the user their
move is invalid. For players interacting with other forms of views, like
textual views, and we had a game in progress called 'game', a sample user interaction may
look like this: “game.placeMove(new RowColPair(3,-1), RepresentativeColor.WHITE)". The controller
would then refresh the view, which, via dynamic dispatch, would call the render() for the textual view.

A user may get help by click the hint button, the valid position and the number of the cell can be
flipped when the user placed a cell here will be marked on that particular hexagon these hints
will only be shown when the current turn is same as the color that the player placing for, ex
black player cannot see the hints for white player, and if black player click the hint button,
the white player still can not see the hints. If the user do not need the hints anymore, just
reclick the hint button, the board will be repainted and the hints will be erased.

    If a user wants to make the legal move at (3, -1) in white as described above, by clicking
    on the tile in our GUI view,
    the path of callbacks and delegation will be as follows:
    -ReversiGUIView is listening for low-level events like Key-press. This triggers the low-level
     event of a pressed key
     -The GUI view translates the low level event of the key press from user clicked at
      (3, -1) and press enter into a high level event in that is a request to place a move, as
      outlined in the features interface. This high-level event would be broadcast to all of the
      view's feature subscribers
      -Since the controller implements the features interface, its callback that the user is trying
      to make a move would get triggered.
    -The controller will check if the move is valid. Since it is, try to apply the move to the model.
    If the model accepts the move as valid, the model will apply it. The model will then notify
    all of its listeners that it has been modified.
    -Since the controllers all listen to the model, the controllers would tell each of their respective
    views to refresh, which might involve repainting for GUIs or rendering for textual views. Once all
     the views are refreshed, the changes to the board would be visible to both players in the game,
     even though only one of the players made the move.

3. Key Components
   Interactions between components
   Main        |         Model         |           Each Controller...  |           Each View         |  Player      |
__________________________________________________________________________________________________________________
config      | addListeners() <------|------ sets itself as listener |                             | AI players    |
step        | ReadOnlyModel <-------|-------------------------------|--------receives instance of | are assigned  |
(before     | MutableModel <--------|-----receives instance of      |                             | a strategy,   |
startGame() |                       |     receives instance of------|---------->GUIView           |human players  |
is called   |                       |     sets itself as a listener---------->addFeatures()       |are each assigned|
|                       |                               |                             | one view and  |
|                       |                               |                             |one controller |
____________|_______________________|_______________________________|_____________________________|_______________|
startGame() |model broadcasts to all|                               |                             |               |
is called on|of its listeners that--|------>each controller updates |                             |                |
the model   |it has been changed-   |     its internal logic        |                             |                |
|                       |           ↓                   |                             |                |
|                       |     each controller tells its |                             |                |
|                       |       corresponding view-----|----->each view repaints------|->each player   |
|                       |        to refresh            |                              | has a GUI they |                            |
|                       |                              |                              | can interact w/|
|                       |                              |                              |                |
___________________________________________________________________________________________________________________|
after       | move/pass applied<----|-----controller notified of <--|-------low level event<------|clicks a button |
|  ↓                    |    features event, tries to   |   converted to features     |or presses a key |
startGame() |  ↓                    |      make the move/pass       |     event, broadcasted to   |to make a move  |
is called   |model broadcasts to all|                               |     features listeners      |       ↑        |
|of its listeners that--|------>each controller updates |                             |       ↑        |
|it has been changed-   |     its internal logic        |                             |       ↑        |
|                       |           ↓                   |                             |       ↑        |
|                       |     each controller tells its |                             |       ↑        |
|                       |       corresponding view-----|----->each view repaints------|->each player   |
|                       |        to refresh             |    and displays             | sees an updated|
|                       |                              |      relevant                | view of board  |
___________________________________________________________________________________________________________________
***NOTE: Eventually, the controller, when updating itself after the model notifies the controller
that the model has been changed, will realize in that the game is over. When this occurs,
the player will stop being able to make moves, since the controlller will not relay
features events to the model anymore. ****


The IView
our Iview Represents a view for the reversi game, it contains several common methods that all the views
may share. For example, update is used to repaint the board to the player once the model was changed, and
show message is used to notify the user if the move is invalid or they can only choose to pass.

Graphic View
our Graphic view contains a board to show the current states for the board of game and a panel that s
hows the current turn in the game and score for both black and white players, and the hasToPassWarning.
It also has a hint button used to determine whether the player wants to get some hints. if the current
turn matched with player's represent color we will notify the user it's your turn to place. If the game
is over, we will also congratulate the winner.

Features
we used a Features Interface in order to translate these low-level events into actions
that can be taken care of in the Controller. This allows our controllers to be decoupled from
views, and be able to be flexibile with any view implementations, This also allows the controller
to not be closely tied to a specific library - like Java Swing, which is good for future extension
and separating the roles of the View and Controller from each other. the controller registered itself
as a listener to both of the model of view, so that the user can interact with view to make actions
in the in the model according to the controller, and once the model was changed, the controller can notify
the view to repaint the board.

The controller
Represents an asynchronous controller for a game of reversi. Controllers are event-driven and
respond to whatever event happens at a given time. The job of controllers are to control the
flow of Reversi Games by determining when and how to update the model, as well as how and when to
update the view that they use. Controllers opearte on the interface level, so they will refresh
their views in a way that is abstracted into the specific implementation. A GUIview implementation
might refresh by repainting, whereas a textual view might refresh by render()ing again.

KeyBoard And Mouse Lock
Once the players in this game is one human player and one Ai player, our controller will let the view and
HexGrid to lock the key board and mouse events for the ai player, so that the human player can not mark
a cell as selected nor actual make an action for ai player. This will prevent the human player from
bothering the ai player.

Start Game and List<ModelListener> in the model
Since there should be a clear distinction between “setting everything up” and “actually playing the game”.
our view and controller can paint the board before the game started. the start game method will first set the
player who will place first to black and notify all the views and controllers that the game is started, the
message related to the game state like the current turn, current score.etc will be shown and the player can
start to place the cell on the board. Whenever the model knows it is changed (for example it successfully
starts the game or makes a move), the model broadcasts to all its listeners that it has been changed.
Each listener decides for themselves what to do when the model is changed.

MockView
We have a MockView in the view package that used for test only, Since we only want to test if the view interact
with other components correctly as we expected, it will only take a string builder as the
parameter, since we don't want the view actually draw the stuffs when we test it. The StringBuilder serves as a log and is used to
help us determine if the view interact with the other components as we expected. For example, if the one of the
play took an action, the controller will call the view to let it toggle the turn, at this time, we will add a
message "You have toggle the turn to the given color" to show that the view interact with our controller correctly.

Player
For the players, we have two classes to represent two different kinds of players, human player and Ai player
and they share a common player interface which contains a very important methods isAiPlayer() this is used to
ask the player to choose for the next move if it is an ai player since human player will interact with the view
and press keys to place move and make pass. And fot the method in the player to let the player choose the next
move, we set the return value to Optional<RowColPair> so that for our human player is can return optional.empty
rather than null, and our controller do not need to handle null values.
The logic of take actions for players is if the current turn matched player's represent color, then that player
can do all the actions (ex. placing move, selecting cell, making pass, show hints) and if the current turn doo not
matched player's represent color, then that player can only mark the cell, all the other commands will be rejected.
The player will not know their color when they joined the game, they will get assigned color when the startGame()
method got called, the player that first join the game will play for the black cell, the player that second join
the game will play for white cell, since our current game can only has two players, only two kinds of color will be
assigned to the players.

***Hint Button***
We add a hint button in to our north panel, the logic of this hint button is first we will find all the position
that can make a move with the current player's color and store the position and the cell that placing here can
flip as a Map<RowColPair, Integer>, then we will based on this position and integers to draw a number on that position
means the cells that can be flipped if you place here. If the player click the button, controller will show
show the hints for the players, and if they click the button again, the controller will let the view hide the hints.
What' more, the show hints or not is independent for each players, that is, if the black player choose to show
the hints, the controller will not show the hints for white player and the view will not show the show the hints for
players if this is not their turn. The painting logic for the hints is, we will drawing the number on the
cell that can make a valid move, since this hint will only be drawn on an empty cell, that for sure, we don't need to
worry about the number was covered by the black circle, which used to mark this cell as been occupied by black player.

4. Key Subcomponents

For each game, each human player will have its own view implementation (ex. a GUI view or a
textual view), which will have its own controller. It is the responsibility of each
controller to update its corresponding view when the controller sees fit for updates.


Whenever a change occurs in the model (for example, the game is started or a move/pass is made),
the model will broadcast that it has been changed to all of its Model Listeners, and the
controllers that listen to model will be notified that the model has been changed and the
controller will update itself.
Updating the controller includes...
-Updating the current score for the black and white players
-Updating whose turn it
-Updating whether the player who will play in the  next turn is a human player or not
-Checking if there are no valid move on the board, and if this is the case,
notifying the model that the player that must pass (since they have no valid moves) .
-Checking if the player who will play in next turn is an Ai player, and if this is the case, asking the player
to choose for the position and place it.
-Refreshing the view that is managed by that controller. The refresh is at the interface level,
so it leverages dynamic dispatch so that specific view implementation can refresh in the way
that it sees fit.

For the model status, we have A ReversiModelStatus class represents the current status for the game model,
once the player placed a move or made a pass or the start the game, the status of the game will be updated
in order to reflect the most recent status. so that in the controller, we will also take in a model status
as the parameter in order to track the status of the model more effectively. we initialize the status to
hasNotStart since every time when start the game, the status will be updated and also, players can not place
move or make pass before the game was started. we have four values to represent the current status for the game
END = The game has ended by 2 consecutive passes.
InProgress = the game has started and there are still legal moves on the board (at least
one legal move for black or for white).
HasNotStarted = startGame() has not been called yet.
Blocked = no valid moves, but the game is not over yet since the game has no passed.
Every time the player take an action(ex. place a move, make a pass or start the game), the status will
update itself so that we can always get the most recent status for our game.

In order to keep the human players away from bothering the ai players, we added some functionality to
lock the mouse event and key board event for ai player, so the human player can never mark a hexagon
or even actually take an action for ai players.

5. Source Organization
   -In the src directory, we can find 3 packages
   -Model → This package contains the interface and domain logic relative to the rules of Reversi
   games, as well as other structures that our reversi models use to represent different features
   of the game. For instance, our model holds the Direction class that is used to handle checking
   if moves exist and executing moves on a certain direction.  This package also contains a Mock
   model. This mock model is used to confirm that strategies are using the proper inputs in
   order to check moves.
   -Controller→ contains different classes that help run the brains of our reversi game.
   We have a Controller class that represents an event-driven asynchronous GUI Controller.
   The Controller serves to control the flow of Reversi Games by determining when and how to
   update the model, as well as how and when to update the GUI view that they use.
   -View → contains different classes that help display our game to the players. The view provides
   interfaces for players to specify their desired move, and to observe the state of the game at
   any time.
   -Strategy -> contains different classes that help a player select a move, by using a specific
   strategy. The strategy package outlines how we can compose strategies, as well as contains
   specific implementations for different fallible and infallible strategies.
   -Test: in the test directory, we may find a few packages:
   Model, controller, and view packages:
   Contains tests for components and their respective subcomponents whose package-visible
   functionality that isn’t part of their public interfaces
   (no package) / the default package:
   Contains tests for the publicly visible signatures of our public interfaces
   This will be the home for unit tests for individual components as well as integration
   tests to measure communication between components (e.g. Between the view and controller)
   Also Contains tests for our strategies, to see if they do in fact consider the values
   of different moves and uphold their class signatures. For example, our fallible classes
   should throw an exception when their strategy move type does not exist for a given
   board state.

Changes for part 3:
We added a start game method in our model in order to make a clear distinction between “setting everything up”
and “actually playing the game”. In this method, we assigned the color for players by player that join the game
first will placing for black cell and player that second join the game will placing for white cell, and set the
player who goes first to black, and notify the controllers and views that care about this model that the game is started.

Also, we added another method in to the readonly model called addListener, because we think listen will not change
the model itself, and this method is used to register the controller as a listener to the model. And in the place
move and make pass method, we added the functionality that so that the model can publish the notification to the
controllers.

We applied a builder pattern to our model, it has two fields, one is for the size of the game, and another
one is for the status class, the purpose of applying builder pattern is to reducing the number of constructor,
and support different combination of the parameter, for example, if one of the way to create the game is using
the default size and default status and another one is using own size and default status, we don't need to
have two different constructors, the size will be initialized to default size and status will be initialized
to default status when call the constructor of the builder and the player can modify these two parameters as they
want.

--------------------------------------------------HW6----------------------------------------------------------------------------------------------------
1. Overview
   The way we generate the view is by first get the coordinators for the middle point, which is (0,0)
   other point will make modifications based on the difference between their coordinators to (0,0)
   and if the cell was occupied(the color of the cell is black or white), view will fill a circle
   based the coordinators of the topping point to show the player already placed a cell in here.
   Essentially, the CubeCoordinateTrio system involves getting coordinates relative to the
   origin point (0,0), which is in the middle tile of any Reversi Board.

   It is assumed that each player in the game has a specific goal in mind with choosing where to place
   their tiles if they are not going to pass on their next turn. Strategies are designed to suggest
   the best move based on a specific goal that a player may have in mind. Strategies can be used
   by any player in the reversi game, whether they are an AI player or a human player.

The key design idea of the view is we have a hexgrid class, which represent a drawing and view only board,
we have two board, one is the model itself and another is the hexgrid class, the idea of this design
is if the user selecting a color, we will only mark that cell in the drawing board instead of
change it in the model since it's unnecessary to do so and may mess up the model by introducing a
new color type.

The logic of selecting and deselecting cell is using the polygon. everytime when user interact with
the window, we will take record of the position that was clicked and convert it to the coordinator
system that (0,0) is in the middle. Next, we will try to find if this point is inside of a hexagon
if it is, get the coordinator of that hexagon. The the selected position will be marked to Cyan in
the drawing board. The view will let the user deselect a selected cell by (1) clicking on the
current selected position again (2) clicking on another valid position new or (3) can not
selecting a position. If the selecting position is not null and if the user typed ENTER,
we will try to place a cell in the current selected position. If the user typed SPACE, that
means the current user choose to pass in current round. The user can npt marked an occupied
cell since we think it's meaningless to do so and it may mess up the drawing board.


2. Quick Start
   A user may get started using our view codebase by interacting with our View's GUI. Once the
   game is begun, players will be able to interact with the GUI by selecting a tile for which to
   place their color. When a tile is selected, its display color will become Cyan, indicating
   that this is their selection. Tiles can be unselected and selected by clicking out of bounds,
   or by clicking to other tiles. When a user wants to make move, they can click the enter key
   when they have a selected tile. If the move is valid, it will go through and the corresponding
   internal game logic and external view of the game will be updated, with the selected tile
   being changed to the color of the player who just made the move. If the move is invalid, they
   will know this because the actual color of the selected tile will not be changed to the color
   of the player whose turn it is. For players interacting with other forms of views, like
   textual views, and we had a game in progress called 'game', a sample user interaction may
   look like this: “game.placeMove(new RowColPair(3,-1), RepresentativeColor.WHITE)"

3. Key Components
   HexGrid
   The key design idea of the view is we have a HexGrid class that only used to show the graph and get
   the selected points. Because rather than make changes on the model directly, doing so can keep
   players from cheating effectively. The logic of the game process in the view is:
   display the current game state, if the user choose a position, rather than change the color of
   this cell in the actual model, only change the color in the display. This design keeps users
   from  cheating, since only valid moves requests are actually send to be made on the model.
   If the player want to place a cell there and it is a valid move, the view will be updated to
   the new game state when the model is updated.

   ReversiView, View interface and Features
   Our Reversiview are classified as either GUIViews, or TextualViews. We have an interface describing
   each. GUIViews are views that are interacted on via button clicks, keyboard presses, etc. and
   are flexible to take in different methods of input, like mouse presses.Even though
   each view gets its input via low-level events, there is always the possibility of future
   view implementations adding more inputs, like hotkeys and keybaord commands. Because of this
   variety, we used a Features Interface in order to translate these low-level events into actions
   that can be taken care of in the Controller. This allows our controllers to be decoupled from
   views, and be able to be flexibile with any view implementations, whether they are textual or GUI
   based. This also allows the controller to not be closely tied to a specific library - like Java
   Swing, which is good for future extension and separating the roles of the View and Controller
   from each other.

   ReversiBoardPanel
   ReversiBoardPanel is used to draw the hexagon board in the window only, it also has the functionality
   to resize the window and to convert the screen coordinators to board coordinator (which means (0,0) is in
   the middle). It also be responsible be handle the user's click by using the Point2D class to capture the
   position that user clicked then convert it to board coordinators to see if it matched with any hexagons.

   Strategies
   In order to represent our strategies, we used 2 interfaces: InfallibleStrategy, and
   FallibleStrategy. Each different strategic approach that we used is either a fallible, or an
   infallible strategy. For shared logic between concrete strategy implementations, we used an
   abstract class called AbstractStrategy. Abstract Strategy represents a strategy for a game of
   reversi. Abstract strategies are exclusively fallible strategies (strategies that may or may not
   return a move for a given model), or  infallible strategies (strategies that are guaranteed
   to return a move for a given model). Strategies serve to help players to decide where to place
   their next move during a game of Reversi.

   In order to combine strategies together, we have a CompositeStrategy class. A Composite
   Strategy represents a comprehensive strategy that is composed of two components: two strategies.
   Composite strategies default to trying their first input strategies as their first option. If that
   strategy finds a move to make, this strategy selects that move. Otherwise, the strategy will use its
   second strategy as a backup. Due to this, Composite strategies can be composed of one single strategy
   with other composite strategies, allowing for flexible and advanced strategies to be created.

   Since all the strategies have a chance to fail, we have a CompleteStrategy class which is a
   concrete implementer of our InfallibleStrategy interface. The InfallibleStrategy interface
   promises its strategy will always return a position to move to for a game of reversi, as long
   as the game is a legal, in-progress game. If the game is not legal or not in-progress, this
   is the only case where InfallibleStrategies may be unable to return a valid position for a move.
   In this case, our complete strategy throws an IllegalStateException to indicate that the reversi
   model is not in a legal state for play.

   MockModel
   We have a MockModel in the model package that used for test only, it will take a regular
   model and the appendable and a list of RowColPair used to determine whether we will lie to the strategy
   as the parameter, so that if the strategy used the method in the mock model it will not only have
   the functionality that regular model has by using the delegate, but also it can add a string message
   to the string builder to let us know which pair does the strategy visited and also to test the strategy
   itself works properly. For example, for our CornerStrategy, the desired behavior is that only moves to
   the corners are checked, and moves to any other positions are not considered. We can leverage our
   MockModel in order to see that when our strategy is trying to choose its move, it calls
   the checkMove() method of the model interface that it is passed. The arguments of the method are appended
   to our appendable log, and then in our tests we can check the values added to our log to see what moves
   were checked and which moves were not checked. If we choose to lie to our strategy, which means the lie list
   is not empty and when the strategy is checking move for a specific position in the lying list, the mock
   model will lie to the strategy that there is no valid move in given position, and append "lie to you"
   to the stringBuilder.


4. Key Subcomponents
   -Move Class: A move Represents an evaluation of a board state for a player for a game of Reversi.
   A move can be expressed as a coordinate on the board onto which a player could place their tile,
   WITH an evaluation of how beneficial the move is to the player that makes it, or simply as an
   evaluation of how beneficial a given board state is.

***Minimax***: the idea of the minimax is in our turn, we want the move that can gives us the maximum value and
there will also be a simulate player for our opponent that will play the move that gives us the
minimum value. it selects the move that would give us the best outcome against an opponent using
the same algorithm and evaluation heuristic. MeanWhile, the minimax is an exhaustive search technique,
thus it is better to restrict its depth, which means the round we are going to simulate.
max       15
/    \
min    5      15
/ \    /  \
max  5   7  15  18
/ \ / \ / \  / \
min 3 5 6 7 1 15 2 18
  _______________________
The game tree represents all possible moves and their consequences. Each level of the tree alternates
between the two players. The root of the tree is the current game state, and each level below it
represents the possible moves of the players. one player is the "maximizer," who tries to maximize
their own score (e.g., win the game), and the other player is the "minimizer," who tries to minimize
the maximizer's score (e.g., prevent the maximizer from winning).Once the tree is traversed up to the
root, the maximizer chooses the child node with the highest value, which represents the best move for
them. The minimizer, in turn, chooses the child node with the lowest value.
A very straightforward example is showing above, the row we choose the max value means is our turn and
otherwise, it's opponent turn.
After we simulate the game to the certain depth, and we read the result from bottom to top. in the root,
it means the possible score we can get in this movement.

***AlphaBeta***: alphabeta strategy is a advanced minimax strategy since it applied puring on the
minimax. Alpha-beta pruning works by maintaining two values, alpha and beta, which represent
the minimum score that the maximizing player is assured of and the maximum score that the
minimizing player is assured of, respectively. As the search progresses, nodes are pruned
(i.e., not evaluated further) if it is determined that they cannot lead to a better outcome than
the current best option. This is achieved by comparing the node's evaluation with the alpha and beta values.

Features: The features interface contains two methods that may modified the model, place move
and make pass.

Strategies: Since it's not a good idea to return a null when there is no valid move to place, our
design idea in the controller will be if there is no valid move for current user, the controller
will notify the user they can only make pass and reject all the other command except pass. Under
this circumstance, our strategies will not failed, thus if the strategies can not find a valid
position to place cell, we will print a message to let us know something wrong with the strategy

In the strategy-transcript.tx, it containing a transcript from our mocked model of the captureMaxPieces strategy
choosing a move for Black on the starting board configuration. with the following code, since the
captureMaxPieces strategy will go over all the position in the board to try to find a valid move, our
stringbuilder will contains the all the position, and since in the initial all the valid position can
only flip one cells, the strategy will choose the upper-most left-most one. And since we choose not to lie
to the strategy, it will choose the position as usual.
StringBuilder builder = new StringBuilder();
MockModel mock = new MockModel(model, builder, new ArrayList<>());
InfallibleStrategy strategy = new CompleteStrategy(new CaptureMaxPieces());
RowColPair pair = strategy.choosePosition(mock, RepresentativeColor.BLACK);
System.out.println(new ReversiTextualView(model));
mock.placeMove(pair, RepresentativeColor.BLACK);
System.out.println(builder.toString());
System.out.println(new ReversiTextualView(model));

Corners: Our board has 6 corners, since there are six places can guarantee the cell will not be flipped
after iw was placed, as the graph shows below
1 _ _ _ _ 6
_ _ _ _ _ _ _
_ _ _ _ _ _ _ _
_ _ _ _ _ _ _ _ _
_ _ _ _ X O _ _ _ _
2 _ _ _ O _ X _ _ _ 5
_ _ _ _ X O _ _ _ _
_ _ _ _ _ _ _ _ _
_ _ _ _ _ _ _ _
_ _ _ _ _ _ _
3 _ _ _ _ 4


Model setup:
The ability to create a board of a given size, in default initial state.(Constructor)
The ability to create a copy of a board.(getBoard())
Observations:
How big is the board?(getSize())
What are the contents of a cell at a given coordinate?(getColorAt())
Is it legal for the current player to play at a given coordinate?(checkMove())
What is the current score for either player?(getScore())
Does the current player have any legal moves?(hasToPass())
Is the game over?(isGameOver())
Operations:
The current player makes a move at a given cell(placeMove())
The current player passes(makePass())

5. Source Organziation
   -In the src directory, we can find 3 packages
   -Model → This package contains the interface and domain logic relative to the rules of Reversi
   games, as well as other structures that our reversi models use to represent different features
   of the game. For instance, our model holds the Direction class that is used to handle checking
   if moves exist and executing moves on a certain direction.  This package also contains a Mock
   model. This mock model is used to confirm that strategies are using the proper inputs in
   order to check moves.
   -Controller→ contains different classes that help run the brains of our reversi game. The
   controller handles, adding the players, and coordinating player input with
   visual output to ensure that each move affects the underlying model and those changes are
   reflected in the view. Currently only have one class used to test.
   -View → contains different classes that help display our game to the players. The view provides
   interfaces for players to specify their desired move, and to observe the state of the game at
   any time.
   -Strategy -> contains different classes that help a player select a move, by using a specific
   strategy. The strategy package outlines how we can compose strategies, as well as contains
   specific implementations for different fallible and infallible strategies.
   -Test: in the test directory, we may find a few packages:
   Model, controller, and view packages:
   Contains tests for components and their respective subcomponents whose package-visible
   functionality that isn’t part of their public interfaces
   (no package) / the default package:
   Contains tests for the publicly visible signatures of our public interfaces
   This will be the home for unit tests for individual components as well as integration
   tests to measure communication between components (e.g. Between the view and controller)
   Also Contains tests for our strategies, to see if they do in fact consider the values
   of different moves and uphold their class signatures. For example, our fallible classes
   should throw an exception when their strategy move type does not exist for a given
   board state.

Changes for part 2:
in the previous homework, our model do not the the ability to get current score for either player,
because in the previous homework, we only used this kind of method as a helper method to determine
the winner of the game when the game is over. so, we added a method called getScore() in the readOnly
model, and it takes a color as parameter. The way we implement that is by counting the number of
the cell in given color in the board.

    we added a new method called getDeepCopy() in the readOnlyReversi, because in the
    Minimax strategy, we need to create a new model to test the overall impact that brought by placing
    in the different position, it certainly can not make modification on the model, so that we added
    this method to help us implement the Minimax strategy.

    -Refactored package private constructor to also take into account the turn of the rigged board.
    -Refactored our deep copy to use our package private constructor for copies,
     this way deep copy remembers whose turn it is.

    -Changed hashcode() to use Objects.hash() instead of adding row and col to reduce collisions
    -Changed name of CubeCoordinatePiar to CubeCoordinateTrio, added javadoc to it
    -Documented more invariants that we had in our RegularReversiModel







--------------------------------------------------HW5----------------------------------------------------------------------------------------

1. Overview
   This code base is designed to simulate games of reversi between players. The general
   ReversiModel 2-layer interface can be implemented to create custom games of reverse. Users of
   the program do not need to be familiar with Java libraries or require any prerequisite
   knowledge beyond that needed to give input to the view implementation of the program.
   How to play: Reversi games may come in multiple flavors in the future, but to play a regular
   reversi game, here are the general rules (not all rules are included): There are 2 players,
   each gets assigned a color. The game is started in an initial starting position, which may
   contain pre-placed tiles on the board. Each player takes turns placing disks of their color on a
   hexagonal board. Players alternate turns. Moves can only be made to unoccupied tiles, and
   players cannot override other players’ turns. The game ends after each player passes, whether by
   choice or due to having no more valid moves to make. Moves must be made adjacent to
   already-placed tiles.

   -Design choice: we decide to let the model to handle the turn, because the turn should be the rules of the game
   thus means the integrity-of-the-game-rules question. For the game over, the only way that to determine if the game
   is over is if two players pass in a same row, so our model will notify the user they can only pass in this time
   rather make the pass action for users. For example, if there is no valid move in the board, the game will not over
   automatically, the controller will let the users know they can only choose to pass in this time and after two user
   choose to pass, the game is over.

2. quickStart
   A user might make a move in a regular reversi game by specifying a specific location to place
   their move and their disc color to be placed at this location. For example, if we had a model
   called “game”, a move could be made by executing the command:
   “game.placeMove(new RowColPair(0,0), CellColor.BLACK).” This command places a black tile at
   the position in the board at (0,0), assuming it is a valid move, and the game is not over yet.

3. key component
   a. For the board, Our game uses cube coordinate system, we choose to use the map to represent
   the hexagon board, each cube system has three coordinators r,q and s, as the graph shows below, and the middle
   point is (0,0,0), the graph shows the coordinators with the order(r,q,s), as the graph shows, row means the
   horizontal line, the q means the leftCol which refers to the number of column we count from
   left to right and the s means rightCol means the number of column from right to left.
   the original point is in the middle of the graph, if the cell go left, the r will increase,
   if the cell go right, the r will decrease. for the q, the leftCol, it will increase when
   the col go left. and for the s, the rightCol, it will increase when the col go right.

           (-2,0,2) (-2,1,1) (-2,2,0)
   (-1,-1,2) (-1,0,1) (-1,1,0) (-1,-2,-1)
   (0,-2,2) (0,-1,1) (0,0,0) (0,1,-1) (0,2,-2)
   (1,-2,1) (1,-1,0) (1,0,-1) (1,1,-2)
   (2,-2,0) (2,-1,-1) (2,0,-2)
   _ _ _
   _ X O _
   _ O _ X _
   _ X O _
   _ _ _
   (After initialize the board with size 3)
   X O
   O _ X
   X O
   (board with size 2 after initialization)

Another coordinator system only have two coordinators, the row and col used to represent the cell on the board
we use r to represent the row and q to represent the col. since our column is counting from left to right,
s is used to track the adjacent hexagon for current from different directions, so we don't add
it to the rowCol system.
For the direction, the left cell for (0,0,0) is (0,-1,1), right cell for (0,0,0) is (0,1,-1),
LeftUp cell for(0,0,0) is (-1,0,1), LeftDown cell for (0,0,0) is (1,-1,0), RightUp cell for (0,0,0) is (-1,1,0),
RightDown cell for (0,0,0) is (1,0,-1).
For the pre-positioned cells, the middle of the board will be empty and the pre-positioned cells will be surround
its. 2 should be the smallest board size to start since 1 can not contains all the pre-positioned cells. User
interact with rowCol pair, because it's more user-friendly.



b.TileColor system: 3 pronged enum: NONE, WHITE, BLACK

c.All cells are initialized to COLOR NONE

d.After the game has been started, any NONE-colored tiles represent empty tiles

e.In the hasToPass method, it will return a boolean value, our model will not be responsible for make the pass for
users, but it will notify the users they can only do pass at this time. And in the controller, if the hasToPass
returns true, no other command except pass will be accepted.

f.Check move return a map to record the number of the cell that can be flipped in a certain direction, because the
order of directions returned by directions.values will not change, we can use it directly with the for loop to
go through all the directions

g. For the logic, the basic logic for the implementation is there are six directions to determine the adjacent
position to the current, and if there is a cell with opposite representativeColor with current representativeColor
in these six positions, keep tracking of that position to check if it forms a line in that direction that contains
number of cell in opposite representativeColor and one cell with the same representativeColor. if there exist such
direction, it means this is a valid move and flip the cell in this direction. And if there is no such valid move
for the given representativeColor in the whole board, the user has to pass, and if two users pass in a same round,
the game is over.

h. class Invariant
1.passTimes can not be larger than 2, the constructor initialize the passTimes to 0, and every time
it will plus one, once the passTimes greater than 2, the game is over.
2.size is positive, the default size is 6, and if the size user entered is less than 1, it will throw
exception
3.turn can only be white or black, turn was initialized to Black, and every time when a user finished a
move or pass, the turn will alter to its opposite, where black's opposite is white and white's opposite is black.

i. our place move and make pass method will take a color as parameter in order to make sure the users can only place
the cell when it's their turns

4. Key subcomponents
   -We use a Direction data type to compute adjacent cells. Essentially, a given cell on our board
   has neighbors from 6 directions (left, right, left-up, right-up, left-down, right-down). In
   order to handle moves and to flip tiles when sandwiches are created, a search is done in each
   direction to determine which tiles can be flipped, if any exist.
   -We use a rigged start with our package-private testing constructor.
   It is used for testing purposes where we can start the game from an initial board position,
   even if it is not necessarily a possible position by game logic. By being able to rig both
   legal and illegal positions, we can ensure that our model handles exceptional and regular
   circumstances gracefully.

5.Source organization
-In the src directory, we can find 3 packages
-Model → This package contains the interface and domain logic relative to the rules of Reversi
games, as well as other structures that our reversi models use to represent different features
of the game. For instance, our model holds the Direction class that is used to handle checking
if moves exist and executing moves on a certain direction.
-Controller→ contains different classes that help run the brains of our reversi game. The
controller handles, adding the players, and coordinating player input with
visual output to ensure that each move affects the underlying model and those changes are
reflected in the view. Currently only have one class used to test.
-View → contains different classes that help display our game to the players. The view provides
interfaces for players to specify their desired move, and to observe the state of the game at
any time.
-Test: in the test directory, we may find a few packages:
Model, controller, and view packages:
Contains tests for components and their respective subcomponents whose package-visible
functionality that isn’t part of their public interfaces
(no package) / the default package:
Contains tests for the publicly visible signatures of our public interfaces
This will be the home for unit tests for individual components as well as integration
tests to measure communication between components (e.g. Between the view and controller)