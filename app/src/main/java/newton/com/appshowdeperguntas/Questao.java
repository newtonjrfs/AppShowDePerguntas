package newton.com.appshowdeperguntas;

public class Questao {


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
