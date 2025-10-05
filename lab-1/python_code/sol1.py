import sys

def sum_multiples(start, end):
    return sum(x for x in range(start, end) if x % 3 == 0 or x % 5 == 0)

if __name__ == "__main__":
    if len(sys.argv) == 3:
        start = int(sys.argv[1])
        end   = int(sys.argv[2])
    else:
        start = 1
        end   = 1000
    print(sum_multiples(start, end))