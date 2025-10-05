def sum_of_fifth_powers():
    upper_limit = 9**5 * 6

    res = 0
    for num in range(2, upper_limit):
        if num == sum(int(d)**5 for d in str(num)):
            res += num

    return res


if __name__ == "__main__":
    total = sum_of_fifth_powers()
    print("Сумма:", total)
