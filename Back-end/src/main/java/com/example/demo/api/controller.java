package com.example.demo.api;


import com.example.demo.service.OpClass;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

@RestController
@RequestMapping("/api/numerical")
@CrossOrigin(origins = { "http://localhost:8080" })
public class controller {
    @PostMapping(path = "/solve")
    public double[] check(@RequestBody final params params) {
        OpClass temp = new OpClass();
        double[][] A = params.getMatrix();
        double [] B = params.getEqual();
        double [] initialGuess = params.getInitial();
        int opNumber = params.getOpNumber();
        int luNumber = params.getLuNumber();
        int precision = params.getPrecision();
        int iterations = params.getIterations();
        double error = params.getError();
        double[] solution = null;
        OpClass c = new OpClass();
        switch (opNumber){
            case 0:{
                solution = c.gaussElimination(A,B,precision);
                break;
            }
            case 1:{
                solution = c.gaussEliminationPivot(A,B,precision);
                break;
            }
            case 2:{
                solution = c.gaussJordan(A,B,precision);
                break;
            }
            case 3:{
                solution = c.lu(A,B,precision,luNumber);
                break;
            }
            case 4:{
                solution =  temp.lastSolution(c.gaussSeidil(A,B,initialGuess,iterations,error,precision));
                break;
            }
            case 5:{
                solution =  temp.lastSolution(c.jacobi(A,B,initialGuess,iterations,error,precision));
                break;
            }
        }
        return solution;
    }


}
