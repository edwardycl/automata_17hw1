# automata_17hw1
regular expression matching program

- make all : compile
- make run : run

you must have input.txt in the same directory to run the program

input.txt는 다음과 같은 형식이다

첫 줄에는 테스트 케이스의 수 T가 주어진다. (T<=20)
2번째 줄부터 T+1번째 줄까지 다음과 같은 형식의 정규식 명령들이 주어진다.

rexp(공백)exp(공백)file(new line character 또는 마지막 줄의 경우 EOF)

- (공백)은 정확히 공백 한칸이다
- exp는 pdf에 설명되어 있는 완벽히 괄호로 둘러싸여 있는 형식의 정규식이다. (1글자~최대 50글자, 작은 따옴표 없음, *연산자 최대 5개)
- file은 pdf에 설명되어 있는 0과 1로만 구성된 연속한 숫자이다. (1글자~최대 100글자)
- 프로그램으로 들어오는 인풋은 항상 위의 조건을 만족한다고 가정하여도 좋다. (예외 처리 안해도 됨)

예시 input.txt

2
rexp ((0.((0+1)*)).1) 00111
rexp ((0.((0+1)*)).1) 0011000
