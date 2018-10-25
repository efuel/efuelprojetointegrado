package com.projeto.efuel.efuelprojeto.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.projeto.efuel.efuelprojeto.R;
import com.projeto.efuel.efuelprojeto.config.ConfiguracaoFireBase;
import com.projeto.efuel.efuelprojeto.helper.UsuarioFirebase;
import com.projeto.efuel.efuelprojeto.model.Usuario;

public class CadastroActivity extends AppCompatActivity {
    //Definindo atributos
    private TextInputEditText campoNome, campoEmail, campoSenha;
    private Switch switchTipoUsuario;

    //Atributos para autenticação no fire base
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        //Inicializando os componentes
        campoNome = findViewById(R.id.nomeCad);
       campoEmail = findViewById(R.id.emailCad);
        campoSenha = findViewById(R.id.senhaCad);
        switchTipoUsuario = findViewById(R.id.switchMF);
    }
//método de validar cadastro do usuário
    public void validarCadastroUsuario(View view){
        //recuperar textos dos campos
        String textoNome = campoNome.getText().toString();
        String textoEmail = campoEmail.getText().toString();
        String textoSenha = campoSenha.getText().toString();

        //Verifica se os dados são válidos
        if(!textoNome.isEmpty()){//Verifica nome
            if(!textoEmail.isEmpty()){//Verifica e-mail
                if(!textoSenha.isEmpty()){//Verifica Senha

                    Usuario usuario = new Usuario();
                    usuario.setNome( textoNome);
                    usuario.setEmail( textoEmail);
                    usuario.setSenha(textoSenha);
                    usuario.setTipo( verificaTipoUsuario());

                    //Salvando usuario
                    cadastrarUsuario(usuario);

                }else{
                    Toast.makeText(CadastroActivity.this, "Preencha a senha! ", Toast.LENGTH_SHORT).show();
                }

            }else{
                Toast.makeText(CadastroActivity.this, "Preencha o e-mail! ", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(CadastroActivity.this, "Preencha o nome! ", Toast.LENGTH_SHORT).show();
        }
    }


    //Método para cadastrar usuario e salvar no fire base
    public void cadastrarUsuario(final Usuario usuario){
        autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //Podemos verificar se foi possivel fazer o cadastro
                if (task.isSuccessful()){
                    try {
                        String idUsuario = task.getResult().getUser().getUid();
                        usuario.setId( idUsuario );
                        usuario.salvar();

                        //Atualizar nome no UserProfile
                        UsuarioFirebase.atualizarNomeUsuario( usuario.getNome());

                        // Redireciona o usuário com base no seu tipo
                        //Se o usuário for passageiro chama a activity maps
                        //senão chama a activity requisicoes
                        if ( verificaTipoUsuario() == "M"){
                            startActivity(new Intent(CadastroActivity.this, MapsActivity.class));
                            finish();
                            Toast.makeText(CadastroActivity.this,"Sucesso ao cadastrar o Motorista!",Toast.LENGTH_SHORT).show();
                        }else {
                            startActivity( new Intent(CadastroActivity.this, RequisicoesActivity.class));
                            finish();
                            Toast.makeText(CadastroActivity.this,"Sucesso ao cadastrar o Frentista!",Toast.LENGTH_SHORT).show();

                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }else {
                    String excecao = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        excecao = "Digite uma senha mais forte!";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        excecao = "Por favor, digite um e-mail válido!";
                    } catch (FirebaseAuthUserCollisionException e) {
                        excecao = "Esta conta já goi cadastrada";
                    } catch (Exception e) {
                        excecao = "Erro ao cadastrar usuário" + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroActivity.this,
                            excecao,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


   //Método SWITCH
    public String verificaTipoUsuario(){
                                     //CASO SEJA VERDADEIRA
        return switchTipoUsuario.isChecked() ? "M" : "F";

    }
}
