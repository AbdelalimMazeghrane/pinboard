#include <assert.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "ecosys.h"

/* PARTIE 1*/
/* Fourni: Part 1, exercice 3, question 2 */
Animal *creer_animal(int x, int y, float energie) {
  Animal *na = malloc(sizeof(Animal));
  //assert(na);
  na->x = x;
  na->y = y;
  na->energie = energie;
  na->dir[0] = rand() % 3 - 1;
  na->dir[1] = rand() % 3 - 1;
  na->suivant = NULL;
  return na;
}


/* Fourni: Part 1, exercice 3, question 3 */
Animal *ajouter_en_tete_animal(Animal *liste, Animal *animal) {
  assert(animal);
  assert(!animal->suivant);
  animal->suivant = liste;
  return animal;
}

/* A faire. Part 1, exercice 5, question 1 */
void ajouter_animal(int x, int y,  float energie, Animal **liste_animal) {
    Animal* new_animal=creer_animal(x,y,energie);
    new_animal->suivant=*(liste_animal);
    *(liste_animal)=new_animal;
}

/* A Faire. Part 1, exercice 5, question 5 */
void enlever_animal(Animal **liste, Animal *animal) {
  
    if (liste == NULL) return;
    if (*liste == NULL || animal == NULL) return;
    Animal *tmp = NULL;
    tmp = *liste;
    if (tmp == animal) {
        *liste = animal->suivant;
        free(animal);
        return;
    }
    while ( tmp->suivant != NULL ) {
        if ( tmp->suivant == animal) {
            tmp->suivant = animal->suivant;
            free(animal);
            return;
        }
        tmp = tmp->suivant;
    }
    return;
}

/* A Faire. Part 1, exercice 5, question 2 */
Animal* liberer_liste_animaux(Animal *liste) {
      Animal* av=NULL;

      while(liste){
        av=liste;
        liste=liste->suivant;
        free(av);
      }
    return NULL;
}

/* Fourni: part 1, exercice 3, question 4 */
unsigned int compte_animal_rec(Animal *la) {
  if (!la) return 0;
  return 1 + compte_animal_rec(la->suivant);
}

/* Fourni: part 1, exercice 3, question 4 */
unsigned int compte_animal_it(Animal *la) {
  int cpt=0;
  while (la) {
    cpt++;
    la=la->suivant;
  }
  return cpt;
}



/* Part 1. Exercice 4, question 1, ATTENTION, ce code est susceptible de contenir des erreurs... */
void afficher_ecosys(Animal *liste_proie, Animal *liste_predateur) {
  unsigned int i, j;
  char ecosys[SIZE_X][SIZE_Y];
  Animal *pa=NULL;

  /* on initialise le tableau */
  for (i = 0; i < SIZE_X; ++i) {
    for (j = 0; j < SIZE_Y; ++j) {
      ecosys[i][j]=' ';
    }
  }

  /* on ajoute les proies */
  pa = liste_proie;
  while (pa) {
    ecosys[pa->x][pa->y] = '*';
    pa=pa->suivant;
  }

  /* on ajoute les predateurs */
  pa = liste_predateur;
  while (pa) {
      if ((ecosys[pa->x][pa->y] == '@') || (ecosys[pa->x][pa->y] == '*')) { /* proies aussi present */
        ecosys[pa->x][pa->y] = '@';
      } else {
        ecosys[pa->x][pa->y] = 'O';
      }
    pa = pa->suivant;
  }

  /* on affiche le tableau */
  printf("+");
  for (j = 0; j < SIZE_Y; ++j) {
    printf("-");
  }  
  printf("+\n");
  for (i = 0; i < SIZE_X; ++i) {
    printf("|");
    for (j = 0; j < SIZE_Y; ++j) {
      putchar(ecosys[i][j]);
    }
    printf("|\n");
  }
  printf("+");
  for (j = 0; j<SIZE_Y; ++j) {
    printf("-");
  }
  printf("+\n");
  int nbproie=compte_animal_it(liste_proie);
  int nbpred=compte_animal_it(liste_predateur);
  
  printf("Nb proies : %5d\tNb predateurs : %5d\n", nbproie, nbpred);

}


void clear_screen() {
  printf("\x1b[2J\x1b[1;1H");  /* code ANSI X3.4 pour effacer l'ecran */
}

/* PARTIE 2*/

/* Part 2. Exercice 4, question 1 */

void bouger_animaux(Animal *la) {
  while(la){
    if(la->energie >0){
      if ((float)rand() / RAND_MAX < P_CH_DIR){
        la->dir[0] = rand()%3 -1;
        la->dir[1] = rand()%3 -1;
      }
      la->x = (la->x + la->dir[0])%SIZE_X;
      la->y = (la->y + la->dir[1])%SIZE_Y;
      if (la->x < 0){
        la->x += SIZE_X;
      }
      if (la->y < 0){
        la->y += SIZE_Y;
      }
    }
    la = la->suivant;
  }
}

/* Part 2. Exercice 4, question 3 */
void reproduce(Animal **liste_animal, float p_reproduce) {
    Animal* tmp=*liste_animal;
    float proba=0.0;
    while(tmp){
        proba=rand()/RAND_MAX;
        if(proba<p_reproduce){
            ajouter_animal(tmp->x,tmp->y,(tmp->energie)/2,liste_animal);
             (*liste_animal)->dir[0] = rand() % 3 - 1;
             (*liste_animal)->dir[1] = rand() % 3 - 1;
            tmp->energie=(tmp->energie)/2;
        }
      tmp=tmp->suivant;
    }

}


/* Part 2. Exercice 6, question 1 */
void rafraichir_proies(Animal **liste_proie, int monde[SIZE_X][SIZE_Y]) {
      Animal* av=NULL;
      Animal* tmp=(*liste_proie);
      if(liste_proie){
      bouger_animaux(*liste_proie);

      while(tmp){
          tmp->energie -= 1;
          if(monde[tmp->x][tmp->y]>=0){
            tmp->energie += monde[tmp->x][tmp->y];
            monde[tmp->x][tmp->y]=TEMPS_REPOUSSE_HERBE;
          }
          
          if(tmp->energie <0){
              av=tmp->suivant;
              enlever_animal(liste_proie,tmp);
              tmp=av;
          }
          else{
            tmp=tmp->suivant;
          }
      }
      reproduce(liste_proie,P_REPRODUCE_PROIE);
    }  
}

/* Part 2. Exercice 7, question 1 */
Animal *animal_en_XY(Animal *l, int x, int y) {
    while(l){
      if((l->x == x) && (l->y == y)){
        return l;
      }
      l=l->suivant; 
    }

  return NULL;
} 

/* Part 2. Exercice 7, question 2 */
void rafraichir_predateurs(Animal **liste_predateur, Animal **liste_proie) {
    Animal* tmp=(*liste_predateur);
    Animal* av=NULL;
    Animal* ani_en_XY=NULL;
    bouger_animaux(*liste_predateur);
      while(tmp){
          tmp->energie -= 1;
          ani_en_XY=animal_en_XY((*liste_proie),(*liste_predateur)->x,(*liste_predateur)->y);
          if(ani_en_XY != NULL){
            tmp->energie += ani_en_XY->energie;
            enlever_animal(liste_proie,ani_en_XY);
            
          }
          if(tmp->energie <0){
              av=tmp->suivant;
              enlever_animal(liste_predateur,tmp);
              tmp=av;
          }
          else{
            tmp=tmp->suivant;
          }
          
      }
      reproduce(liste_predateur,P_REPRODUCE_PREDATEUR);

}

/* Part 2. Exercice 5, question 2 */
void rafraichir_monde(int monde[SIZE_X][SIZE_Y]){

   int i,j;
   for(i=0;i<SIZE_X;i++){
    for(j=0;j<SIZE_Y;j++){
      monde[i][j]++;
    }
  }
}



