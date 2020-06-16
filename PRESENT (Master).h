#include <stdio.h>
// membaca bit di coding tetap seperti pembacaan array. DI paper hanya pengindeksan

unsigned long int SBOX[] = {0xc,0x5,0x6,0xb,
							0x9,0x0,0xa,0xd,
							0x3,0xe,0xf,0x8,
							0x4,0x7,0x1,0x2};
unsigned long int SBOXInv[] = {0x5,0xe,0xf,0x8,
							0xc,0x1,0x2,0xd,
							0xb,0x4,0x6,0x3,
							0x0,0x7,0x9,0xa};

unsigned long int kunci[32][5], kuncidekrip[32][5];

unsigned long int KeyAddition(unsigned long int *Plain,int round){
	Plain[3] ^= kunci[round][3];
	Plain[2] ^= kunci[round][2];
	Plain[1] ^= kunci[round][1];
	Plain[0] ^= kunci[round][0];
}

unsigned long int KeyAdditionInv(unsigned long int *Cipher,int round){
	Cipher[3] ^= kuncidekrip[round][3];
	Cipher[2] ^= kuncidekrip[round][2];
	Cipher[1] ^= kuncidekrip[round][1];
	Cipher[0] ^= kuncidekrip[round][0];
}

unsigned long int SBox(unsigned long int *Plain){

	unsigned long int temp;

	temp=0;
    temp |= (SBOX[(Plain[0]&0xF)]);
	temp |= (SBOX[(Plain[0]>>=4)&0xF]<<4);
	temp |= (SBOX[(Plain[0]>>=4)&0xF]<<8);
	temp |= (SBOX[(Plain[0]>>=4)&0xF]<<12);
	Plain[0]=temp;
	temp=0;
    temp |= (SBOX[(Plain[1]&0xF)]);
	temp |= (SBOX[(Plain[1]>>=4)&0xF]<<4);
	temp |= (SBOX[(Plain[1]>>=4)&0xF]<<8);
	temp |= (SBOX[(Plain[1]>>=4)&0xF]<<12);
	Plain[1]=temp;
	temp=0;
    temp |= (SBOX[(Plain[2]&0xF)]);
	temp |= (SBOX[(Plain[2]>>=4)&0xF]<<4);
	temp |= (SBOX[(Plain[2]>>=4)&0xF]<<8);
	temp |= (SBOX[(Plain[2]>>=4)&0xF]<<12);
	Plain[2]=temp;
	temp=0;
    temp |= (SBOX[(Plain[3]&0xF)]);
	temp |= (SBOX[(Plain[3]>>=4)&0xF]<<4);
	temp |= (SBOX[(Plain[3]>>=4)&0xF]<<8);
	temp |= (SBOX[(Plain[3]>>=4)&0xF]<<12);
	Plain[3]=temp;
}

unsigned long int SBoxInv(unsigned long int *Plain){

	unsigned long int temp1;

temp1=0;
	temp1 |= (SBOXInv[(Plain[0]&0xF)]);
	temp1 |= (SBOXInv[(Plain[0]>>=4)&0xF]<<4);
	temp1 |= (SBOXInv[(Plain[0]>>=4)&0xF]<<8);
	temp1 |= (SBOXInv[(Plain[0]>>=4)&0xF]<<12);
	Plain[0]=temp1;
	temp1=0;
	temp1 |= (SBOXInv[(Plain[1]&0xF)]);
	temp1 |= (SBOXInv[(Plain[1]>>=4)&0xF]<<4);
	temp1 |= (SBOXInv[(Plain[1]>>=4)&0xF]<<8);
	temp1 |= (SBOXInv[(Plain[1]>>=4)&0xF]<<12);
	Plain[1]=temp1;
	temp1=0;
	temp1 |= (SBOXInv[(Plain[2]&0xF)]);
	temp1 |= (SBOXInv[(Plain[2]>>=4)&0xF]<<4);
	temp1 |= (SBOXInv[(Plain[2]>>=4)&0xF]<<8);
	temp1 |= (SBOXInv[(Plain[2]>>=4)&0xF]<<12);
	Plain[2]=temp1;
	temp1=0;
	temp1 |= (SBOXInv[(Plain[3]&0xF)]);
	temp1 |= (SBOXInv[(Plain[3]>>=4)&0xF]<<4);
	temp1 |= (SBOXInv[(Plain[3]>>=4)&0xF]<<8);
	temp1 |= (SBOXInv[(Plain[3]>>=4)&0xF]<<12);
	Plain[3]=temp1;
}
unsigned long int Permutation(unsigned long int *Plain){

	unsigned long int temp, state[4];

	for(int i=0 ; i<4 ; i++){
		temp=0;
       	temp |= (Plain[0]<<i)&0x8000;
		temp |= ((Plain[0]<<(4+i))&0x8000) >>1;
		temp |= ((Plain[0]<<(8+i))&0x8000) >>2;
		temp |= ((Plain[0]<<(12+i))&0x8000) >>3;
		temp |= ((Plain[1]<<i)&0x8000) >>4;
		temp |= ((Plain[1]<<(4+i))&0x8000) >>5;
		temp |= ((Plain[1]<<(8+i))&0x8000) >>6;
		temp |= ((Plain[1]<<(12+i))&0x8000) >>7;
		temp |= ((Plain[2]<<i)&0x8000) >>8;
		temp |= ((Plain[2]<<(4+i))&0x8000) >>9;
		temp |= ((Plain[2]<<(8+i))&0x8000) >>10;
		temp |= ((Plain[2]<<(12+i))&0x8000) >>11;
		temp |= ((Plain[3]<<i)&0x8000) >>12;
		temp |= ((Plain[3]<<(4+i))&0x8000) >>13;
		temp |= ((Plain[3]<<(8+i))&0x8000) >>14;
		temp |= ((Plain[3]<<(12+i))&0x8000) >>15;
		state[i] = temp;
	}
	for(int i=0 ; i<4 ; i++){
		Plain[i] = state[i];
	}
}

unsigned long int PermutationInv(unsigned long int *Plain){

	unsigned long int temp,i, state[4];
	
	i=0;
	for(int j=0 ; j<4 ; j++){
		temp=0;
		temp |= (Plain[0]<<i)&0x8000;
		temp |= ((Plain[1]<<(i))&0x8000) >>1;
		temp |= ((Plain[2]<<(i))&0x8000) >>2;
		temp |= ((Plain[3]<<(i))&0x8000) >>3;	
		temp |= ((Plain[0]<<(1+i))&0x8000) >>4;
		temp |= ((Plain[1]<<(1+i))&0x8000) >>5;
		temp |= ((Plain[2]<<(1+i))&0x8000) >>6;
		temp |= ((Plain[3]<<(1+i))&0x8000) >>7;	
		temp |= ((Plain[0]<<(2+i))&0x8000) >>8;
		temp |= ((Plain[1]<<(2+i))&0x8000) >>9;
		temp |= ((Plain[2]<<(2+i))&0x8000) >>10;
		temp |= ((Plain[3]<<(2+i))&0x8000) >>11;
		temp |= ((Plain[0]<<(3+i))&0x8000) >>12;
		temp |= ((Plain[1]<<(3+i))&0x8000) >>13;
		temp |= ((Plain[2]<<(3+i))&0x8000) >>14;
		temp |= ((Plain[3]<<(3+i))&0x8000) >>15;
		state[j] = temp;
		i=i+4;
	}
	for(int j=0; j<4; j++){
		Plain[j]=state[j];
	}
}

unsigned long int KeySchedule(unsigned long int *Key, int round){
	
//	FILE *cetak;
//	cetak = fopen("Subkuci PRESENT.txt","w");
	
	unsigned long int decBox3[] = { 0x0000,0x2000,0x4000,0x6000,
									0x8000,0xa000,0xc000,0xe000};
	unsigned long int temp, key[80];
	
	for(int i=0;i<5;i++){
		kunci[0][i]=Key[i];
//		fprintf(cetak,"%04x ", kunci[0][i]);
	}
	//fprintf(cetak,"\n");
	
	for(int r=1; r<round+1; r++){
		key[0] = Key[4]>>3;
		key[0] |= decBox3[Key[3]&0x07];
			
		key[1] = Key[0]>>3;
		key[1] |= decBox3[Key[4]&0x07];
			
		key[2] = Key[1]>>3;
		key[2] |= decBox3[Key[0]&0x07];
			
		key[3] = Key[2]>>3;
		key[3] |= decBox3[Key[1]&0x07];
			
		key[4] = Key[3]>>3;
		key[4] |= decBox3[Key[2]&0x07];
			
		Key[0] = key[0];
		Key[1] = key[1];
		Key[2] = key[2];
		Key[3] = key[3];
		Key[4] = key[4];
	
		//sBox
		temp = Key[0]>>12;
		Key[0] &= 0x0FFF;
		Key[0] |= SBOX[temp]<<12;
	
		//Permutation
		if(r%2 == 1)Key[4] ^= 0x8000;
		Key[3] ^= ( r>> 1 );
		
		for(int j=0;j<5;j++){
			kunci[r][j]=Key[j];
//			fprintf(cetak, "%04x ", kunci[r][j]);
		}
		//fprintf(cetak,"\n");
	}
}

void copy_KeySchedule(int round){
	
	for(int i=0; i<round+1; i++){
		for(int j=0; j<5; j++){
			kuncidekrip[i][j]=kunci[round-i][j];
		}
	}
}


unsigned long int encryptPRESENT(unsigned long int *Plain, unsigned long int *Key, int round){

	KeySchedule(Key,round);
	
	for(int r=0 ; r<round; r++){
		KeyAddition(Plain,r);	
		SBox(Plain); 
		Permutation(Plain);  
	} 
	KeyAddition(Plain,round);
}

unsigned long int decryptPRESENT(unsigned long int *Cipher, unsigned long int *Key, int round){
	
	KeySchedule(Key, round);
	copy_KeySchedule(round);
	
	KeyAdditionInv(Cipher, 0); 
	
	for(int r=1 ; r<round+1; r++){	
		PermutationInv(Cipher);
		SBoxInv(Cipher);  
		KeyAdditionInv(Cipher,r);
	} 
}
