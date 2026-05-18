#include <stdio.h>
#include <string.h>

void make_new_name(char *new_name, char *original_name);
int is_alpha(char c);
int is_digit(char c);
int is_valid_password(char *password);
void perform_XOR(char *input_filename, char *output_filename, char *password);
void print_first_five(char *filename);

int main(int argc, char *argv[]) {
    if (argc != 3) {
        printf("Usage: ./A2 filename password\n");
        return 1;
    }

    char *original_filename = argv[1];
    char *password = argv[2];

    char new_filename[25];
    make_new_name(new_filename, original_filename);
    printf("New filename = %s\n", new_filename);

    if (!is_valid_password(password)) {
        return 1;
    }

    perform_XOR(original_filename, new_filename, password);
    print_first_five(new_filename);

    return 0;
}

void make_new_name(char *new_name, char *original_name) {
    strcpy(new_name, "new-");
    strcat(new_name, original_name);
}

int is_alpha(char c) {
    return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
}

int is_digit(char c) {
    return (c >= '0' && c <= '9');
}

int is_valid_password(char *password) {
    int len = strlen(password);
    printf("Password length = %d\n", len);

    int has_alpha = 0;
    int has_digit = 0;

    for (int i = 0; i < len; i++) {
        if (is_alpha(password[i])) has_alpha = 1;
        if (is_digit(password[i])) has_digit = 1;
    }

    int valid = 1;

    if (len < 8) {
        printf("The password needs to have at least 8 characters.\n");
        valid = 0;
    }
    if (!has_alpha) {
        printf("The password needs to contain at least 1 alphabetical character.\n");
        valid = 0;
    }
    if (!has_digit) {
        printf("The password needs to contain at least 1 digit.\n");
        valid = 0;
    }

    return valid;
}

void perform_XOR(char *input_filename, char *output_filename, char *password) {
    FILE *in = fopen(input_filename, "rb");
    FILE *out = fopen(output_filename, "wb");

    if (!in) {
        printf("Failed to open input file: %s\n", input_filename);
        return;
    }
    if (!out) {
        printf("Failed to open output file: %s\n", output_filename);
        fclose(in);
        return;
    }

    int pass_len = strlen(password);
    if (pass_len == 0) {
        fclose(in);
        fclose(out);
        return;
    }

    int ch;
    int i = 0;

    while ((ch = fgetc(in)) != EOF) {
        ch = ch ^ password[i % pass_len];
        fputc(ch, out);
        i++;
    }

    fclose(in);
    fclose(out);
}

void print_first_five(char *filename) {
    FILE *fp = fopen(filename, "rb");
    if (!fp) {
        printf("Failed to open file: %s\n", filename);
        return;
    }

    for (int i = 0; i < 5; i++) {
        int byte = fgetc(fp);
        if (byte == EOF) break;
        printf("%02x\n", byte);
    }

    fclose(fp);
}
