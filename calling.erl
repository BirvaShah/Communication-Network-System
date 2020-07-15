-module(calling).
-export([time_stamp/0,person/0]).

person() ->
    receive
        {Send,Receive} ->
            Intro = "intro",
            lists:map(fun (Receiver) -> Timestamp = time_stamp(),list_to_atom(string:to_lower(lists:flatten(io_lib:format("~p", [Receiver])))) ! {Send,Receiver,Timestamp,Intro} end, Receive),
            person();
        {Sender,Receiver,Timestamp,Intro} ->
            Reply = "reply",
            timer:sleep(round(timer:seconds(rand:uniform()))),
            list_to_atom(string:to_lower(lists:flatten(io_lib:format("~p", [Receiver])))) ! {Sender,Receiver,Timestamp,Reply,Reply},
            master ! {Sender,Receiver,Timestamp,Intro},
            person();
        {Sender,Receiver,Timestamp,Reply,Reply} ->
            timer:sleep(round(timer:seconds(rand:uniform()))),
            master ! {Sender,Receiver,Timestamp,Reply,Reply},
            person();
        stop ->
            {_,Reg_name} = erlang:process_info(self(), registered_name),
            io:format("~nProcess ~p has received no calls for 1 second, ending...~n~n",[Reg_name]),
            exit(kill)
    end.
  time_stamp() ->
    {_,_,Micro} = erlang:now(),
    Micro.
