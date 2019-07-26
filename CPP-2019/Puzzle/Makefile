all: test

main: board.hpp board.cpp solver.hpp solver.cpp main.cpp
	$(CXX) -g -Wall -Wextra -Werror -std=c++17 $^ -o main

test: main
	./main

clean:
	rm main

.PHONY: all test main clean
