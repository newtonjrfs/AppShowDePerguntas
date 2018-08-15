package newton.com.appshowdeperguntas;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView perguntas;
    RadioGroup grupoResposta;
    RadioButton respA;
    RadioButton respB;
    RadioButton respC;
    Button confirmar;
    ProgressBar progresso;

    int rodada = 0 ;
    int respostasCertas = 0;
    int respostaEscolhida = 0 ;

    ArrayList<Questao> listaQuestoes;

    String txtTexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        perguntas = findViewById(R.id.text_pergunta);
        grupoResposta = findViewById(R.id.grupoAlternativas);
        respA = findViewById(R.id.opcao1);
        respB = findViewById(R.id.opcao2);
        respC = findViewById(R.id.opcao3);
        confirmar = findViewById(R.id.botao_prosseguir);
        progresso = findViewById(R.id.progressBar);

        String url = "http://www.json-generator.com/api/json/get/bVziMKMcKq?indent=2";

        listaQuestoes = new ArrayList<Questao>();

        confirmar.setEnabled(false);

        grupoResposta.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.opcao1){
                    respostaEscolhida = 1;
                }
                if (checkedId == R.id.opcao2){
                    respostaEscolhida = 2;
                }
                if (checkedId == R.id.opcao3){
                    respostaEscolhida = 3;
                }

                confirmar.setEnabled(true);

            }
        });


        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (respostaEscolhida == listaQuestoes.get(rodada).correta){
                    int a = listaQuestoes.get(rodada).correta;
                    respostasCertas ++;
                }
                clicaProxima();

            }
        });

        new JsonTask().execute(url);

    }
    private void clicaProxima() {



        rodada++;

        if (rodada >= listaQuestoes.size()){
            fimDeJogo();

        }else{
            atualizaView();

        }

    }

    private void fimDeJogo() {

        progresso.setVisibility(View.GONE);
        confirmar.setEnabled(false);
        criaAlerta();


    }

    private void criaAlerta() {
        AlertDialog.Builder alerta;
        alerta = new AlertDialog.Builder(MainActivity.this);
        alerta.setTitle("Fim de Jogo");
        alerta.setMessage("Você marcou " + respostasCertas + " pontos");
        alerta.setIcon(R.mipmap.ic_ajuda);
        alerta.setCancelable(false);

        alerta.setPositiveButton("Jogar Novamente", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                rodada = 0;
                respostasCertas = 0;
                atualizaView();

            }
        });
        alerta.create();
        alerta.show();
    }

    void atualizaView(){

        progresso.setVisibility(View.GONE);
        grupoResposta.clearCheck();
        perguntas.setText(listaQuestoes.get(rodada).pergunta);
        respA.setText(listaQuestoes.get(rodada).respostaA);
        respB.setText(listaQuestoes.get(rodada).respostaB);
        respC.setText(listaQuestoes.get(rodada).respostaC);
        confirmar.setEnabled(false);

    }



    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();

                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                    Log.d("Response: ", "> " + line);
                }
                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.i("meuLog",""+result);

            try {

                JSONObject listaJson = new JSONObject(result);
                txtTexto = listaJson.getString("titulo");
                JSONArray questionario = listaJson.getJSONArray("questionario");

                for (int i = 0 ; i < questionario.length();i++){

                    JSONObject questao = questionario.getJSONObject(i);

                    Questao minhaQuestao = new Questao(
                            questao.getString("Pergunta"),
                            questao.getString("respA"),
                            questao.getString("respB"),
                            questao.getString("respC"),
                            questao.getInt("correta"));
                    listaQuestoes.add(minhaQuestao);

                    atualizaView();
                }


            }catch (JSONException e){
                e.printStackTrace();
            }

        }
    }


}
