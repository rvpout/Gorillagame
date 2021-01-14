package fi.utu.tech.distributed.gorilla.logic;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import javafx.application.Platform;
import mesh.Mesh;
import mesh.MeshPackage;
//import mesh.Mesh.Handler;

public class GorillaMultiplayerLogic extends GorillaLogic{
	
	private Mesh verkko;
	Tila tila = Tila.NOTHING;
	Map<Long, String> pelaajat = new HashMap<Long, String>();
	Map<Long, Player> pelaajaOliot = new HashMap<Long, Player>();
			
	public void processMessages(MeshPackage mp){


        if(mp.data instanceof ChatMessage) {
            ChatMessage msg = (ChatMessage)mp.data;
            System.out.printf("%s sanot: %s%n", msg.sender, msg.contents);
        }
        
        if(mp.data instanceof MultiplayerCommands) {
        	MultiplayerCommands command = (MultiplayerCommands)mp.data;
        	if(tila==Tila.NOTHING) {
        	switch(command){
            case HOST:
            	tila = Tila.AVAIL;
            	System.out.println("Joku HOSTAA");
            	break;
            case JOIN:
            	tila=Tila.JOINED;
            	break;
        		}
        	}
        	if(tila==Tila.HOSTING) {
        		//System.out.println("Olet HOST");
            	switch(command){
                case HOST:
                	//tila = Tila.AVAIL;
                	break;
                case JOIN:
                	System.out.println("Joku joinaa");
                	//tila=Tila.JOINED;
                	//(String name, LinkedBlockingQueue<Move> moves, boolean local
                	pelaajat.put(mp.sender, Double.toString(mp.sender));
                	
                	if(pelaajat.size() == 2) {
                		try {
                			System.out.println("2 pelaajaa");
                			GameConfiguration gc = new GameConfiguration( gameSeed, getCanvas().getHeight(), pelaajat);
							verkko.broadcast(gc);
							initMp(gc);
					        
					        //public GameState(GameConfiguration configuration, List<Player> players, Player me) {

							//initGame();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                	}
                	
                	break;
            		}
        		}
        	/**
        	if(tila==Tila.JOINED) {
        		GameConfiguration gc = new GameConfiguration( gameSeed, getCanvas().getHeight(), pelaajat);
        		initMp(gc);
        	}
        	**/
        
        }
        if(mp.data instanceof GameConfiguration) {
        	GameConfiguration gc = (GameConfiguration)mp.data;
        	initMp(gc);
        }
        
        if(mp.data instanceof MoveThrowBanana) {
        	MoveThrowBanana mtb = (MoveThrowBanana)mp.data;
        	pelaajaOliot.get(mp.sender).moves.add(mtb);
        }
        
   }
	
	
	public void initMp(GameConfiguration gc) {
		ArrayList<Long> lista = new ArrayList<Long>(gc.playerIdNames.keySet());
		Collections.sort(lista);
		ArrayList<Player> pelaajaLista = new ArrayList<Player>();
		Player me = null;
		for(Long i : lista) {
			Player p = new Player(gc.playerIdNames.get(i), new LinkedBlockingQueue<Move>(), i == verkko.meshId);
			pelaajaOliot.put(i, p);
			pelaajaLista.add(p);
			if(i == verkko.meshId) {
				me = p;
			}
		}
        gameState = new GameState(gc, pelaajaLista, me);
        views.setGameState(gameState);
        setMode(GameMode.Game);
	}
	
	 @Override
	 protected void startServer(String port) {
	        System.out.println("Starting server at port " + port);
	        try {
	            if (verkko == null)
	                this.verkko=new Mesh(Integer.parseInt(port), false, this);
	            verkko.start();
	        } catch (NumberFormatException e) {
	            System.out.println(e);
	            e.printStackTrace();
	        } catch (Exception e) {
	            System.out.println(e);
	            e.printStackTrace();
	        }
	        
	    }
	 @Override
	 protected void connectToServer(String address, String port) {
	        System.out.printf("Connecting to server at %s", address, port);

	        try {

	            verkko.connect(address, Integer.parseInt(port));


	        } catch (UnknownHostException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }

	        // ...or at least somebody should be
	    }
	 @Override
	 protected void handleChatMessage(ChatMessage msg) throws IOException {
	        System.out.printf("Sinä sanot: %s%n", msg.contents);

	        verkko.broadcast(msg);

	 }
	 
	 @Override
	 protected void handleMultiplayer() {
	    	
		 if(tila == Tila.NOTHING) {
	    	try {
	    		pelaajat.put(verkko.meshId, Double.toString(verkko.meshId));
				verkko.broadcast(MultiplayerCommands.HOST);
				System.out.println("HOSTING");
				tila=Tila.HOSTING;
	    	
	    	} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
		 }
		 if(tila == Tila.AVAIL) {
	    		try {
					verkko.broadcast(MultiplayerCommands.JOIN);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		System.out.println("JOINED");
	    		tila=Tila.JOINED;
	    	}
	        //System.out.println("Not implemented on this logic");
	    }
	 
	 protected void handleThrowBanana(MoveThrowBanana mtb) {
	        gameState.addLocalPlayerMove(mtb);
	        try {
				verkko.broadcast(mtb);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	 
	 public enum Tila{NOTHING, HOSTING, AVAIL, JOINED}
}

