package erincminareci.ThreadJoining;

import java.util.*;

public class Node extends Thread{
    String Name;
    ArrayList<Node> waitfor=new ArrayList<>();
    ArrayList<Node> whowaits=new ArrayList<>();
    boolean isFinished=false;
    public Object lock=new Object();

    Random random = new Random();
    int numOfWaitFor;
    public Node(String name ) {
        this.Name=name;
    }


    public String writewaitfor(ArrayList<Node> waitFor ){
        String x="";
        for(Node n:waitFor){
            x += n.Name+" ";
        }
        return x;
    }

    public void workOrwait(){

        while (numOfWaitFor>0 ) {
            synchronized (lock) {
                System.out.println("Node" + this.Name + " is waiting for Node " + this.writewaitfor(this.waitfor));
                try {
                    lock.wait(waitfor.size()*2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        perform();
    }
    public void unlock(){
        for (Node n : this.whowaits) {
            n.numOfWaitFor--;
            if (n.numOfWaitFor == 0 && !isFinished) {
                n.lock.notify();
            }
        }


    }
    public void perform() {
        if (!this.isFinished) {
            System.out.println("Node" + this.Name + " being started");
            try {
                Thread.sleep(random.nextInt(2000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Node" +this.Name + " is completed");
            this.isFinished=true;
            unlock();

        }
    }
    @Override
    public void run() {
        workOrwait();
    }

}
