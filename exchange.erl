-module(exchange).
-export([start/0,loop/1,master/0]).
-import('calling', [person/1]).

master()->
    receive
        {File} ->
            {Sender,Receiver} = File,
             register(Sender,spawn(calling,person,[])),
            list_to_atom(string:to_lower(lists:flatten(io_lib:format("~p", [Sender])))) ! {Sender,Receiver},
            master();
        {Sender,Receiver,Timestamp,Intro} ->
            io:format("~p received ~s message from ~p [~p]~n",[Receiver,Intro,Sender,Timestamp]),
            master();
        {Sender,Receiver,Timestamp,Reply,R} ->
            io:format("~p received ~s message from ~p [~p]~n",[Sender,Reply,Receiver,Timestamp]),
            master()
    after
        1500 ->
            {_, File} = file:consult("calls.txt"),
            stop_person(File),
            io:format("~nMaster has received no replies for 1.5 seconds, ending...~n"),
            exit(kill)
    end.
  stop_person(File) ->
    lists:map(fun(X) -> {Sender,_} = X, list_to_atom(string:to_lower(lists:flatten(io_lib:format("~p", [Sender])))) ! stop end, File),
    timer:sleep(1).
  
loop([]) ->
     ok;
 loop(File)->
    [H|T] = File,
    {Head,Tail} = H,
    io:fwrite("~p:~p~n",[Head,Tail]),
    loop(T).

start()->
io:fwrite("** Calls to be made **\n"),
 {_,File} = file:consult("calls.txt"),
 loop(File),
 io:fwrite("~n",[]),
 register(list_to_atom("master"),spawn(exchange,master,[])),
    lists:map(fun (Pair) -> master ! {Pair} end, File),
    timer:sleep(1).

