import sys

def sum_of_fifth_powers(start, end):
    res = 0
    for num in range(start, end):
        if num == sum(int(d)**5 for d in str(num)):
            res += num
    return res


if __name__ == "__main__":
    if len(sys.argv) == 3:
        start = int(sys.argv[1])
        end   = int(sys.argv[2])
    else:
        start = 1000
        end   = 400_000
    print(sum_of_fifth_powers(start, end))