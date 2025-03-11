package com.example.Projet_Junit;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;


@ContextConfiguration

public class CompteBancaireTest {
	private CompteBancaire compte; 
	 private NotificationService notificationServiceMock;

	 
	 
	 
	 @BeforeEach
	    void setUp() {
	        notificationServiceMock = mock(NotificationService.class); 
	        compte = new CompteBancaire(100.0, notificationServiceMock);
	    }

@Test 
	void testCreationCompteAvecSoldeInitial(){
	CompteBancaire compte =new CompteBancaire(100.00);
	assertEquals(100.0,compte. getSolde());
		
}
@Test
void testDepotArgent() {
    compte.deposer(50.00);
    assertEquals(150.0, compte.getSolde());
    verify(notificationServiceMock).envoyerNotification("Dépôt de 50.0 effectué.");
}

	 
	 
	




@Test
void testRetraitArgent() {
    compte.retirer(70);
    assertEquals(30.0, compte.getSolde());
    verify(notificationServiceMock).envoyerNotification("Retrait de 70.0 effectué.");
}



@Test
void testCreationCompteAvecSoldeNegatif() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
        new CompteBancaire(-50.0); // Solde négatif
    });
    assertEquals("Le solde initial ne peut pas être négatif.", exception.getMessage());
}




@Test
void testDepotMontantNegatif() {
    CompteBancaire compte = new CompteBancaire(100.0);
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
        compte.deposer(-10.0);
    });
    assertEquals("Le montant du dépôt doit être positif.", exception.getMessage());
}



@Test
void testRetraitMontantNegatif() {
    CompteBancaire compte = new CompteBancaire(100.0);
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
        compte.retirer(-10.0);
    });
    assertEquals("Le montant du retrait doit être positif.", exception.getMessage());
}



@Test
void testDepot() {
    compte.deposer(50.0); 
    verify(notificationServiceMock).envoyerNotification("Dépôt de 50.0 effectué.");
}

@Test
void testRetraitSuperieurSolde() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> compte.retirer(200.0));
    assertEquals("Fonds insuffisants.", exception.getMessage());
    verify(notificationServiceMock, never()).envoyerNotification(anyString()); 
}




@Test
void testDepotDeuxFois() {
    compte.deposer(20.0);
    compte.deposer(30.0);
    verify(notificationServiceMock, times(2)).envoyerNotification(anyString()); 
}



@Test
void testTransfert() {
    NotificationService autreNotificationServiceMock = mock(NotificationService.class);
    CompteBancaire autreCompte = new CompteBancaire(50.0, autreNotificationServiceMock);

    compte.transfererVers(autreCompte, 50.0);

    assertEquals(50.0, compte.getSolde());
    assertEquals(100.0, autreCompte.getSolde());
    verify(notificationServiceMock).envoyerNotification("Retrait de 50.0 effectué.");
    verify(autreNotificationServiceMock).envoyerNotification("Dépôt de 50.0 effectué.");
}

}











	
