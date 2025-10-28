package org.technoready;

import static spark.Spark.*;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        port(8080);
        get("/hello", (req, res)->"Hello, world");
        }
    }