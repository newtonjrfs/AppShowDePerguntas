package newton.com.appshowdeperguntas;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clicaProxima();

            }
        });


        grupoResposta.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.opcao1){
                    Toast.makeText(MainActivity.this, "Aperto no 1", Toast.LENGTH_SHORT).show();
                }
                if (checkedId == R.id.opcao2){
                    Toast.makeText(MainActivity.this, "Aperto no 2", Toast.LENGTH_SHORT).show();
                }
                if (checkedId == R.id.opcao3){
                    Toast.makeText(MainActivity.this, "Aperto no 3", Toast.LENGTH_SHORT).show();
                }

                confirmar.setEnabled(true);

            }
        });

        new JsonTask().execute(url);

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
    private void clicaProxima() {

        rodada++;
        atualizaView();

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

}

class Questao{

    String pergunta ;
    String respostaA;
    String respostaB;
    String respostaC;
    int correta;

    public Questao(String pergunta, String respostaA, String respostaB, String respostaC, int correta) {
        this.pergunta = pergunta;
        this.respostaA = respostaA;
        this.respostaB = respostaB;
        this.respostaC = respostaC;
        this.correta = correta;
    }

    public String getPergunta() {
        return pergunta;
    }

    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }

    public String getRespostaA() {
        return respostaA;
    }

    public void setRespostaA(String respostaA) {
        this.respostaA = respostaA;
    }

    public String getRespostaB() {
        return respostaB;
    }

    public void setRespostaB(String respostaB) {
        this.respostaB = respostaB;
    }

    public String getRespostaC() {
        return respostaC;
    }

    public void setRespostaC(String respostaC) {
        this.respostaC = respostaC;
    }

    public int getCorreta() {
        return correta;
    }

    public void setCorreta(int correta) {
        this.correta = correta;
    }
}