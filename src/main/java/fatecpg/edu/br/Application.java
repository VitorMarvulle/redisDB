package fatecpg.edu.br;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import redis.clients.jedis.commands.CommandCommands;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Connection;

import java.util.Locale;
import java.util.Scanner;

@SpringBootApplication
public class Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        Jedis jedis = new Jedis("redis://default:5FPc6UnKwn6eRgA800beDh8jxTzCaBzc@redis-18200.c308.sa-east-1-1.ec2.cloud.redislabs.com:18200");
        Connection connection = jedis.getConnection();
        Scanner scan = new Scanner(System.in);

        System.out.print("Informe a quantidade de TAREFAS que serão adicionadas: ");
        int qtd = scan.nextInt();
        scan.nextLine();
        String[] tarefas = new String[qtd];


        for(int i=0; i<qtd; i++) {
            System.out.print("Digite a Descrição da "+(i+1)+"º TAREFA: ");
            tarefas[i] = scan.nextLine();
            jedis.set("TF"+(i+1), tarefas[i]);
        }

        System.out.println("LISTANDO TODAS AS TAREFAS");
        for(int i=0; i<qtd; i++) {
            System.out.println("****");
            System.out.println("ID: TF"+(i+1));
            System.out.println(("Descrição:")+jedis.get("TF"+(i+1)));
        }

        System.out.print("Deseja marcar alguma tarefa como CONCLUÍDA? S/N: ");
        String resp = scan.nextLine();
            if(resp.toUpperCase(Locale.ROOT).equals("S")){
                System.out.print("Digite o ID da tarefa: ");
                String done = scan.nextLine().toUpperCase();
                for(int i=0; i<qtd; i++) {
                    System.out.println("****");
                    if(("TF"+(i+1)).equals(done)) {
                        System.out.println("ID: TF"+(i+1));
                        jedis.set("TF"+(i+1), "TAREFA CONCLUÍDA");
                        System.out.println("TAREFA CONCLUÍDA");
                    }else{
                        System.out.println("ID: TF"+(i+1));
                        System.out.println(("Descrição:")+jedis.get("TF"+(i+1)));
                    }

                }

                System.out.print("Digite o ID da tarefa que deseja EXCLUIR: ");
                String del = scan.nextLine().toUpperCase();
                jedis.del(del);

                for(int i=0; i<qtd; i++) {
                System.out.println("****");
                System.out.println("ID: TF"+(i+1));
                System.out.println(("Descrição:")+jedis.get("TF"+(i+1)));
            }


        }

        connection.close();
    }

}
