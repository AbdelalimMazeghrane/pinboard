#include <assert.h>
#include <stdio.h>
#include <stdlib.h>
#include "ecosys.h"
#include <time.h>
#include <unistd.h>
#include <string.h>

int main (void){
    //declaration de variables
    int i;
    int energie=10;
    srand(time(NULL));
    Animal* proies=NULL;
    Animal* predateurs=NULL;
    char ligne[255];
    int x,y,dir0,dir1;
    float ener;
    Animal* proies_bis=NULL;
    Animal* predateurs_bis=NULL;
    
    
    //ouvrir les fichiers pour ecrire et lire
     FILE* fichecrire=fopen("ecosys.txt","w");
    if(fichecrire==NULL){
        printf("erreure");
        exit(1);
    }

     FILE* fichlire=fopen("ecosys.txt","r");
    if(fichlire==NULL){
        printf("erreure");
        exit(1);
    }
   
    
    //creation de deux listes (proies et predateurs) et afficher l'ecosystem
    for(i=0;i<20;i++){
        proies=ajouter_en_tete_animal(proies,creer_animal(rand()%20,rand()%50,energie));
        predateurs=ajouter_en_tete_animal(predateurs,creer_animal(rand()%20,rand()%50,energie));
    }
    afficher_ecosys(proies,predateurs);
    
    
      //enlever un animal
    Animal* env=proies->suivant->suivant;
    enlever_animal(&proies,env);
    afficher_ecosys(proies,predateurs);
   
   
    //ecrire dans un fichier
    Animal* tmpproies=proies;
    Animal* tmppredateurs=predateurs;
     
     
     fprintf(fichecrire,"<proies>\n");
    while(tmpproies){
        fprintf(fichecrire,"x=%d  y=%d  dir=[%d %d]  e=%f\n",tmpproies->x,tmpproies->y,tmpproies->dir[0],tmpproies->dir[1],tmpproies->energie);
        tmpproies=tmpproies->suivant;

    }
    fprintf(fichecrire,"</proies>\n");

    fprintf(fichecrire,"<predateurs>\n");
    while(tmppredateurs){
        fprintf(fichecrire,"x=%d  y=%d  dir=[%d %d]  e=%f\n",tmppredateurs->x,tmppredateurs->y,tmppredateurs->dir[0],tmppredateurs->dir[1],tmppredateurs->energie);
        tmppredateurs=tmppredateurs->suivant;

    }
    fprintf(fichecrire,"</predateurs>\n");

   
    //lire dans un fichier
    while(fgets(ligne,255,fichlire) != NULL){
        if(strncmp(ligne,"<proies>",8)==0){
            while((strncmp(ligne,"</proies>",9)!=0)){
                fgets(ligne,255,fichlire);
                sscanf(ligne," x=%d  y=%d  dir=[%d %d]  e=%f",&x,&y,&dir0,&dir1,&ener);
                ajouter_animal(x,y,ener,&proies_bis);
                proies_bis->dir[0]=dir0;
                proies_bis->dir[1]=dir1;
            }
            fgets(ligne,255,fichlire);
             while((strncmp(ligne,"</predateurs>",13)!=0)){
                fgets(ligne,255,fichlire);
                sscanf(ligne," x=%d  y=%d  dir=[%d %d]  e=%f",&x,&y,&dir0,&dir1,&ener);
                ajouter_animal(x,y,ener,&predateurs);
                predateurs_bis->dir[0]=dir0;
                predateurs_bis->dir[1]=dir1;
            }
        }
        else{
             while((strncmp(ligne,"</predateurs>",13)!=0)){
                fgets(ligne,255,fichlire);
                sscanf(ligne," x=%d  y=%d  dir=[%d %d]  e=%f",&x,&y,&dir0,&dir1,&ener);
                ajouter_animal(x,y,ener,&predateurs_bis);
                predateurs_bis->dir[0]=dir0;
                predateurs_bis->dir[1]=dir1;
            }
            fgets(ligne,255,fichlire);
             while((strncmp(ligne,"</proies>",9)!=0)){
                fgets(ligne,255,fichlire);
                sscanf(ligne," x=%d  y=%d  dir=[%d %d]  e=%f",&x,&y,&dir0,&dir1,&ener);
                ajouter_animal(x,y,ener,&predateurs_bis);
                predateurs_bis->dir[0]=dir0;
                predateurs_bis->dir[1]=dir1;
            }
        }
    }
   
   
    //liberer les listes
    proies= liberer_liste_animaux(proies);
    predateurs=liberer_liste_animaux(predateurs);
    
    proies_bis=liberer_liste_animaux(proies_bis);
    proies_bis=liberer_liste_animaux(predateurs_bis);
   
   //fermer les fichiers
    fclose(fichecrire);
    fclose(fichlire);
    return 0;
} 