<template>
  <v-app>
    <v-app-bar app color="white" style="height: 60px" dark>
      <v-toolbar-title class="text-uppercase teal--text">
        <span class="font-weight-light cyan--text">System of Equation </span>
        <span>Solver</span>
      </v-toolbar-title>
    </v-app-bar>

    <v-main
      :style="{
        backgroundImage: 'url(' + require('@/assets/f.jpg') + ')',
        backgroundSize: 'cover',
      }"
    >
      <v-card :max-width="ww" class="mx-auto my-2 py-2">
        <v-window v-model="step" class="ma-0 pa-0">
          <v-window-item :value="1">
            <!-- selecte number of equations -->
            <v-select
              :items="values"
              v-model="ntext"
              label="Number of equations"
              style="width: 175px"
              color="cyan"
              class="ml-4"
              scrollable
              @click="valuesetter()"
            ></v-select>
            <v-divider></v-divider>
            <v-layout>
              <v-spacer></v-spacer>
              <v-btn
                @click="
                  step++;
                  set();
                  ww = numberofequ * '200';
                "
                color="teal accent-3"
                class="mr-4"
                text
              >
                Next <v-icon>mdi-next</v-icon>
              </v-btn>
            </v-layout>
          </v-window-item>
          <v-window-item :value="2">
            <v-btn
              @click="
                step--;
                equal=[];
                numberofequ = undefined;
                ww = '500';
                
              "
              color="teal accent-3"
              text
              class="mr-4"
            >
              <v-icon large>mdi-arrow-left</v-icon>
            </v-btn>
            <v-form ref="form" v-model="form">
              <v-card-title
                  class="cyan--text accent-5 my-0 py-0 ml-5 pl-5"
                  style="font-size: 12px"
                  >Write Coefficients</v-card-title
                >
              <v-card-text
                text
                v-for="x in numberofequ"
                :key="x"
                elevation="19"
              >
                <v-layout justify-space-around>
                  <h5 style="font-size: 12px; margin: 5px; padding: 1px">
                    Equation<sub>{{ x }}</sub>
                  </h5>
                  <v-card-text
                    text
                    v-for="n in numberofequ"
                    :key="n"
                    class="ma-0 pa-0"
                  ><!-- text fields of the 2D array -->
                    <v-text-field
                      regular
                      v-model="projects[x - 1][n - 1]"
                      color="cyan"
                      :rules="[rules.required, rules.check]"
                      style="width: 50px; height: 20px"
                      class="ma-0 pa-0 ml-1"
                    ></v-text-field>
                  </v-card-text>
                  <!-- text fields of the answers of this equations -->
                  <v-text-field
                    regular
                    v-model="equal[x-1]"
                    :rules="[rules.required, rules.check]"
                    color="cyan"
                    label=""
                    style="width: 100px; height: 20px"
                    class="ma-0 pa-0 mx-5"
                  ></v-text-field>
                </v-layout>
              </v-card-text>
            </v-form>
            <v-divider></v-divider>
            <v-layout >
              <v-spacer></v-spacer>
              <v-btn
              :disabled="!form"
                text
                right
                bottom
                @click="
                  test();
                  step++;
                  ww = '500';
                "
                color="teal accent-3"
                class="mr-4"
              >
                Solve
              </v-btn>
            </v-layout>
          </v-window-item>
          <v-window-item :value="3">
            <v-card-title
              class="cyan--text accent-5 my-0 py-1"
              style="font-size: 30px"
              >Solution</v-card-title
            ><div v-if="solution.length">
              <v-card-text
              class="ma-5 pa-5"
              style="font-size: 20px"
              v-for="x in numberofequ"
              :key="x"
              
             >
              X<sub>{{ x }}</sub> = {{ solution[x - 1] }}
            </v-card-text>
            </div>
            
            <v-card-text v-else  class="ma-5 pa-5"
              style="font-size: 25px">
              Wrong input
            </v-card-text>
            <v-divider></v-divider>
            <v-layout >
              <v-spacer></v-spacer>
              <v-btn color="teal accent-3" text @click="step--;ww=numberofequ*'200'">resolve</v-btn>
              <v-btn color="teal accent-3" text @click="step-=2;ntext=1;equal=[]; $refs.form.reset();set()">Exit</v-btn>
            </v-layout>
          </v-window-item>
        </v-window>
      </v-card>
      <v-card dark max-width="600" class="mx-auto" v-if="step != 3">
        <v-container>
          <v-layout>
            <v-radio-group dark v-model="optionradio">
              <v-radio
                v-for="n in 6"
                :key="n - 1"
                :label="options[n - 1]"
                :value="n - 1"
                color="cyan"
              ></v-radio>
            </v-radio-group>
            <v-radio-group
              v-if="optionradio === 3"
              dark
              v-model="lu"
              class="ml-4"
            >
              <v-radio
                v-for="n in 3"
                :key="n - 1"
                :label="luoptions[n - 1]"
                :value="n - 1"
                color="cyan"
              ></v-radio>
            </v-radio-group>
            <v-container
              v-if="optionradio === 5 || optionradio === 4"
              class="mx-5"
            >
              <v-form class="ma-0 pa-0" text dark>
                <v-card-title
                  class="cyan--text accent-5 my-0 py-1"
                  style="font-size: 15px"
                  >Stopping condition</v-card-title
                >
                <!-- text fields of the Number of iterations-->
                <v-text-field
                  dark
                  regular
                  v-model="numiteration"
                  :rules="[rules.required, rules.check]"
                  color="cyan"
                  label="Number of iterations"
                  style="width: 120px; height: 50px"
                  class="ma-0 pa-0 mr-5"
                ></v-text-field>
                <!-- text fields of the absolute relative error -->
                <v-text-field
                  dark
                  regular
                  v-model="relativerror"
                  :rules="[rules.required, rules.check]"
                  color="cyan"
                  label="Absolute Relative Error"
                  style="width: 120px; height: 50px"
                  class="ma-0 pa-0 mu-5"
                ></v-text-field>
              </v-form>
            </v-container>
            <v-container
              v-if="optionradio === 5 || optionradio === 4"
              class="ml-5"
            >
              <v-form v-for="y in ntext" class="ma-0 pa-0" text dark :key="y">
                <v-card-title
                  v-if="y === 1"
                  class="cyan--text accent-5 my-0 py-1"
                  style="font-size: 20px"
                  >Intial Guess</v-card-title
                >
                <!-- text fields of the intial values -->
                <v-text-field
                  dark
                  regular
                  v-model="intial[y - 1]"
                  :rules="[rules.required, rules.check]"
                  color="cyan"
                  :label="`X${y}`"
                  style="width: 50px; height: 50px"
                  class="ma-0 pa-0 mr-5"
                ></v-text-field>
              </v-form>
            </v-container>
          </v-layout>
          <!--text field for percision-->
           <v-text-field
                    v-model="precision"
                   :rules="[rules.required, rules.check]"
                    color="cyan"
                    label="precision"
                    style="width: 50px;"
                    class="ma-0 pa-0 mu-1"
                  ></v-text-field>
        </v-container>
      </v-card>
    </v-main>
  </v-app>
</template>

<script>
import axios from 'axios'
export default {
  name: "App",

  data: () => ({
    //the two day array of Coefficients
    projects: [[""]],
    //b array
    equal: [],
    //intial guessing
    intial: [],
    //type of LU Decompostion
    lu: 0,
    //type of solving method
    optionradio: 0,
    //number of iterations
    numiteration: "30",
    //Absolute RelativeError
    relativerror: "0.05",
    //precision value default 16
    precision:"16",
    //the returned final solution
    solution: [],
    //arrays of options
    values: [],
    options: [
      "Gauss Elimination",
      "Gauss Elimination using pivoting",
      "Gauss Jordan",
      "LU Decomposition",
      "Gauss Seidil",
      "Jacobi Iteration",
    ],
    luoptions: ["Downlittle Form", "Crout Form", "Cholesky Form"],
    step: 1, //window
    ww: "500", //window width
    numberofequ: 0, //number of equations
    ntext: 1, //v-selecte
    form: false,
    rules: {
      check: (v) =>  !isNaN(v)|| "Wrong", //if it's alphaptic
      required: (v) => !!v || "required",
    },
  }),
  methods: {
    /**
     * set the dimantion of the project array
     */
    set() {
      this.numberofequ = this.ntext;
      this.projects = new Array(this.numberofequ);
      for (let i = 0; i < this.numberofequ; i++) {
        this.projects[i] = new Array(this.numberofequ);
        for(let j=0;j<this.numberofequ;j++){
          this.projects[i][j]=""
        }
      }
      for(let i=0;i<this.numberofequ;i++){
        this.equal[i]=""
      }
       for(let i=0;i<this.numberofequ;i++){
        this.intial[i]="1"
      }
    },
    /**
     * set the options of number of equations in values array
     */
    valuesetter() {
      for (let i = 1; i <= 100; i++) {
        this.values.push(i);
      }
    },
    test(){
      axios.post('http://localhost:9000/api/numerical/solve', {
      matrix: this.projects,
      equal: this.equal,
      initial: this.intial,
      opNumber: this.optionradio,
      luNumber: this.lu,
      precision: this.precision,
      iterations: this.numiteration,
      error: this.relativerror,
      })
      .then((response) => {
        this.solution = response.data;
      })
      .catch((err) =>{
        console.log(err)
      });
    },
  },
};
</script>
