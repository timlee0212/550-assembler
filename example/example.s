lw $8, 1($0)
lw $9, 2($0)
lw $10, 3($0)
lw $11, 4($0)
lw $12, 5($0)
lw $13, 6($0)
lw $14, 7($0)

add $23, $1, $0
add $24, $8, $0
add $25, $15, $0
jal check
add $22, $22, $26
add $15, $25, $0

add $23, $2, $0
add $24, $9, $0
add $25, $16, $0
jal check
add $22, $22, $26
add $16, $25, $0

add $23, $3, $0
add $24, $10, $0
add $25, $17, $0
jal check
add $22, $22, $26
add $17, $25, $0

add $23, $4, $0
add $24, $11, $0
add $25, $18, $0
jal check
add $22, $22, $26
add $18, $25, $0

add $23, $5, $0
add $24, $12, $0
add $25, $19, $0
jal check
add $22, $22, $26
add $19, $25, $0

add $23, $6, $0
add $24, $13, $0
add $25, $20, $0
jal check
add $22, $22, $26
add $20, $25, $0

add $23, $7, $0
add $24, $14, $0
add $25, $21, $0
jal check
add $22, $22, $26
add $21, $25, $0

j 0

check:
#is the key supposed to be down?
bne $0, $24, sk1
addi $25, $0, 0
addi $26, $0, 0
jr $31
#is the key down?
sk1: bne $23, $0, sk2
addi $26, $0, 0
jr $31
#has the score been incremented already?
sk2: bne $25, $23, sk3
addi $26, $0, 0
jr $31
sk3: addi $25, $0, 1
addi $26, $0, 1
jr $31