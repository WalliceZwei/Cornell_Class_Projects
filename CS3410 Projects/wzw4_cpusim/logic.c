#include "sol.h"
#include <stdio.h>

/// TODO
uint32_t fetch(uint64_t PC, uint32_t *instructions) {
  return instructions[PC / 4];
}

// TODO
info decode(uint32_t instruction) {
  uint32_t opcode = instruction & 127;
  uint32_t rs1 = (instruction >> 15) & 31;
  uint32_t rs2 = (instruction >> 20) & 31;
  info result;
  result.inst = instruction;
  // I-Type
  if (opcode == 19 || opcode == 3) {
    result.first = registers[rs1];
    result.second = (int32_t)instruction >> 20;
  }
  // S-type
  else if (opcode == 35) {
    int32_t imm = ((instruction >> 7) & 31) | ((instruction >> 25) << 5);
    imm = (int32_t)(imm << 20) >> 20;
    result.first = registers[rs1];
    result.second = registers[rs2];
    result.third = imm;
  }
  // R-type
  else if (opcode == 51) {
    result.first = registers[rs1];
    result.second = registers[rs2];
  }
  // LUI
  else if (opcode == 55) {
    result.second = (int64_t)(int32_t)(instruction & 0xFFFFF000);
  }
  // BEQ
  else if (opcode == 99) {
    int32_t imm =
        ((instruction >> 31) & 1) << 12 | ((instruction >> 7) & 1) << 11 |
        ((instruction >> 25) & 0x3F) << 5 | ((instruction >> 8) & 0xF) << 1;

    imm = (imm << 19) >> 19;
    result.first = registers[rs1];
    result.second = registers[rs2];
    result.third = imm;
  }
  return result;
}

// TODO
info execute(info information) {

  uint32_t funct3 = (information.inst >> 12) & 7;
  uint32_t opcode = information.inst & 127;
  uint32_t funct7 = (information.inst >> 25) & 127;

  // I-Type
  if (opcode == 3) {
    information.first += information.second;
  } else if (opcode == 19) {
    // addi
    if (funct3 == 0) {
      information.first += information.second;
    }
    // andi
    else if (funct3 == 7) {
      information.first = information.first & information.second;
    }
    // ori
    else if (funct3 == 6) {
      information.first = information.first | information.second;
    }
    // xori
    else if (funct3 == 4) {
      information.first = information.first ^ information.second;
    }
  }
  // S-type
  else if (opcode == 35) {
    information.first += information.third;
  }
  // R-type
  else if (opcode == 51) {
    if (funct3 == 0) {
      // add
      if (funct7 == 0) {
        information.first += information.second;
      }
      // subtract
      else if (funct7 == 32) {
        information.first -= information.second;
      }
    }
    // sll
    else if (funct3 == 1) {
      information.first = information.first << (information.second & 63);
    }
    // slt
    else if (funct3 == 2) {
      information.first =
          ((int64_t)information.first < (int64_t)information.second) ? 1 : 0;
    }
    // sra
    else if (funct3 == 5) {
      information.first =
          ((int64_t)information.first) >> (information.second & 63);
    }
    // and
    else if (funct3 == 7) {
      information.first = information.first & information.second;
    }
  }
  // U-type (LUI)
  else if (opcode == 55) {
    information.first = information.second;
  }
  // beq

  else if (opcode == 99) {
    if (information.first == information.second) {
      information.first = 1;
    } else {
      information.first = 0;
    }
  }

  return information;
}

// TODO
info memory(info information) {
  uint32_t funct3 = (information.inst >> 12) & 7;
  uint32_t opcode = information.inst & 127;
  uint64_t addr = information.first;
  uint32_t temp = 0;
  uint64_t temp2 = 0;
  // LOAD
  if (opcode == 3) {
    if (funct3 == 0) {
      information.first = (int64_t)(int8_t)ht_get(data, addr);
    } else if (funct3 == 2) {
      for (int i = 0; i < 4; i++) {
        uint64_t saved = ht_get(data, addr + i);
        // printf("The number is: %" PRIu64 "\n", saved);
        temp |= (uint32_t)((saved & 0xFF) << (i * 8));
      }

      information.first = (int64_t)temp;

    } else if (funct3 == 3) {
      for (int i = 0; i < 8; i++) {
        temp2 |= ((uint64_t)ht_get(data, addr + i) & 0xFF) << (i * 8);
      }
      information.first = temp2;
    }
  }
  // STORE
  else if (opcode == 35) {
    uint64_t val = information.second;
    if (funct3 == 0) {
      ht_insert(data, addr, val & 0xFF);

    } else if (funct3 == 2) {
      for (int i = 0; i < 4; i++) {
        ht_insert(data, addr + i, (val >> (i * 8)) & 0xFF);
      }
    } else if (funct3 == 3) {
      for (int i = 0; i < 8; i++) {
        ht_insert(data, addr + i, ((val >> (i * 8)) & 0xFF));
      }
    }
  }
  return information;
}

// TODO
uint64_t writeback(uint64_t PC, info information) {
  uint32_t opcode = information.inst & 127;
  uint32_t rd = (information.inst >> 7) & 31;
  if (opcode == 3 || opcode == 19 || opcode == 51 || opcode == 55) {
    if (rd != 0) {
      registers[rd] = information.first;
    }
  }

  // beq

  else if (opcode == 99) {
    if (information.first == 1) {
      return PC + information.third;
    }
  }
  return PC + 4;
}
