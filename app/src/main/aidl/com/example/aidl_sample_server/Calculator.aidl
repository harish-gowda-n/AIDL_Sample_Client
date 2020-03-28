// Calculator.aidl
package com.example.aidl_sample_server;

// Declare any non-default types here with import statements

interface Calculator {

        int add(int a, int b);
        int sub(int a, int b);
        int mul(int a, int b);
        int div(int a, int b);
}
