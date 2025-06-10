CC=gcc
CFLAGS=-Wall -Wextra -O2
TARGET=app

all: $(TARGET)

$(TARGET): src/main.c
	$(CC) $(CFLAGS) -o $(TARGET) src/main.c

clean:
	rm -f $(TARGET)

.PHONY: all clean
