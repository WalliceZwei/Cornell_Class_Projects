
browser:     file format elf64-littleriscv
browser
architecture: riscv:rv64, flags 0x00000112:
EXEC_P, HAS_SYMS, D_PAGED
start address 0x0000000000010ab0

Program Header:
    PHDR off    0x0000000000000040 vaddr 0x0000000000010040 paddr 0x0000000000010040 align 2**3
         filesz 0x0000000000000230 memsz 0x0000000000000230 flags r--
  INTERP off    0x0000000000000270 vaddr 0x0000000000010270 paddr 0x0000000000010270 align 2**0
         filesz 0x0000000000000021 memsz 0x0000000000000021 flags r--
0x70000003 off    0x00000000000020c5 vaddr 0x0000000000000000 paddr 0x0000000000000000 align 2**0
         filesz 0x0000000000000061 memsz 0x0000000000000000 flags r--
    LOAD off    0x0000000000000000 vaddr 0x0000000000010000 paddr 0x0000000000010000 align 2**12
         filesz 0x00000000000014c4 memsz 0x00000000000014c4 flags r-x
    LOAD off    0x0000000000001df0 vaddr 0x0000000000012df0 paddr 0x0000000000012df0 align 2**12
         filesz 0x00000000000002a8 memsz 0x0000000000000358 flags rw-
 DYNAMIC off    0x0000000000001e08 vaddr 0x0000000000012e08 paddr 0x0000000000012e08 align 2**3
         filesz 0x00000000000001e0 memsz 0x00000000000001e0 flags rw-
    NOTE off    0x0000000000000294 vaddr 0x0000000000010294 paddr 0x0000000000010294 align 2**2
         filesz 0x0000000000000020 memsz 0x0000000000000020 flags r--
EH_FRAME off    0x0000000000001370 vaddr 0x0000000000011370 paddr 0x0000000000011370 align 2**2
         filesz 0x0000000000000044 memsz 0x0000000000000044 flags r--
   STACK off    0x0000000000000000 vaddr 0x0000000000000000 paddr 0x0000000000000000 align 2**4
         filesz 0x0000000000000000 memsz 0x0000000000000000 flags rwx
   RELRO off    0x0000000000001df0 vaddr 0x0000000000012df0 paddr 0x0000000000012df0 align 2**0
         filesz 0x0000000000000210 memsz 0x0000000000000210 flags r--

Dynamic Section:
  NEEDED               libc.so.6
  PREINIT_ARRAY        0x0000000000012df0
  PREINIT_ARRAYSZ      0x0000000000000008
  INIT_ARRAY           0x0000000000012df8
  INIT_ARRAYSZ         0x0000000000000008
  FINI_ARRAY           0x0000000000012e00
  FINI_ARRAYSZ         0x0000000000000008
  HASH                 0x00000000000102b8
  GNU_HASH             0x0000000000010360
  STRTAB               0x0000000000010648
  SYMTAB               0x0000000000010420
  STRSZ                0x00000000000000c9
  SYMENT               0x0000000000000018
  DEBUG                0x0000000000000000
  PLTGOT               0x0000000000012ff0
  PLTRELSZ             0x00000000000001b0
  PLTREL               0x0000000000000007
  JMPREL               0x00000000000107b8
  RELA                 0x0000000000010770
  RELASZ               0x00000000000001f8
  RELAENT              0x0000000000000018
  VERNEED              0x0000000000010740
  VERNEEDNUM           0x0000000000000001
  VERSYM               0x0000000000010712

Version References:
  required from libc.so.6:
    0x069691b4 0x00 03 GLIBC_2.34
    0x06969187 0x00 02 GLIBC_2.27

Sections:
Idx Name          Size      VMA               LMA               File off  Algn
  0 .interp       00000021  0000000000010270  0000000000010270  00000270  2**0
                  CONTENTS, ALLOC, LOAD, READONLY, DATA
  1 .note.ABI-tag 00000020  0000000000010294  0000000000010294  00000294  2**2
                  CONTENTS, ALLOC, LOAD, READONLY, DATA
  2 .hash         000000a8  00000000000102b8  00000000000102b8  000002b8  2**3
                  CONTENTS, ALLOC, LOAD, READONLY, DATA
  3 .gnu.hash     000000bc  0000000000010360  0000000000010360  00000360  2**3
                  CONTENTS, ALLOC, LOAD, READONLY, DATA
  4 .dynsym       00000228  0000000000010420  0000000000010420  00000420  2**3
                  CONTENTS, ALLOC, LOAD, READONLY, DATA
  5 .dynstr       000000c9  0000000000010648  0000000000010648  00000648  2**0
                  CONTENTS, ALLOC, LOAD, READONLY, DATA
  6 .gnu.version  0000002e  0000000000010712  0000000000010712  00000712  2**1
                  CONTENTS, ALLOC, LOAD, READONLY, DATA
  7 .gnu.version_r 00000030  0000000000010740  0000000000010740  00000740  2**3
                  CONTENTS, ALLOC, LOAD, READONLY, DATA
  8 .rela.dyn     00000048  0000000000010770  0000000000010770  00000770  2**3
                  CONTENTS, ALLOC, LOAD, READONLY, DATA
  9 .rela.plt     000001b0  00000000000107b8  00000000000107b8  000007b8  2**3
                  CONTENTS, ALLOC, LOAD, READONLY, DATA
 10 .plt          00000140  0000000000010970  0000000000010970  00000970  2**4
                  CONTENTS, ALLOC, LOAD, READONLY, CODE
 11 .text         000006dc  0000000000010ab0  0000000000010ab0  00000ab0  2**2
                  CONTENTS, ALLOC, LOAD, READONLY, CODE
 12 .rodata       000001e0  0000000000011190  0000000000011190  00001190  2**3
                  CONTENTS, ALLOC, LOAD, READONLY, DATA
 13 .eh_frame_hdr 00000044  0000000000011370  0000000000011370  00001370  2**2
                  CONTENTS, ALLOC, LOAD, READONLY, DATA
 14 .eh_frame     0000010c  00000000000113b8  00000000000113b8  000013b8  2**3
                  CONTENTS, ALLOC, LOAD, READONLY, DATA
 15 .preinit_array 00000008  0000000000012df0  0000000000012df0  00001df0  2**0
                  CONTENTS, ALLOC, LOAD, DATA
 16 .init_array   00000008  0000000000012df8  0000000000012df8  00001df8  2**3
                  CONTENTS, ALLOC, LOAD, DATA
 17 .fini_array   00000008  0000000000012e00  0000000000012e00  00001e00  2**3
                  CONTENTS, ALLOC, LOAD, DATA
 18 .dynamic      000001e0  0000000000012e08  0000000000012e08  00001e08  2**3
                  CONTENTS, ALLOC, LOAD, DATA
 19 .got          00000008  0000000000012fe8  0000000000012fe8  00001fe8  2**3
                  CONTENTS, ALLOC, LOAD, DATA
 20 .got.plt      000000a0  0000000000012ff0  0000000000012ff0  00001ff0  2**3
                  CONTENTS, ALLOC, LOAD, DATA
 21 .sdata        00000008  0000000000013090  0000000000013090  00002090  2**3
                  CONTENTS, ALLOC, LOAD, DATA
 22 .sbss         00000020  0000000000013098  0000000000013098  00002098  2**3
                  ALLOC
 23 .bss          00000090  00000000000130b8  00000000000130b8  00002098  2**3
                  ALLOC
 24 .comment      0000002d  0000000000000000  0000000000000000  00002098  2**0
                  CONTENTS, READONLY
 25 .riscv.attributes 00000061  0000000000000000  0000000000000000  000020c5  2**0
                  CONTENTS, READONLY
 26 .debug_aranges 00000030  0000000000000000  0000000000000000  00002126  2**0
                  CONTENTS, READONLY, DEBUGGING, OCTETS
 27 .debug_info   000006f4  0000000000000000  0000000000000000  00002156  2**0
                  CONTENTS, READONLY, DEBUGGING, OCTETS
 28 .debug_abbrev 00000229  0000000000000000  0000000000000000  0000284a  2**0
                  CONTENTS, READONLY, DEBUGGING, OCTETS
 29 .debug_line   000004a7  0000000000000000  0000000000000000  00002a73  2**0
                  CONTENTS, READONLY, DEBUGGING, OCTETS
 30 .debug_str    00000400  0000000000000000  0000000000000000  00002f1a  2**0
                  CONTENTS, READONLY, DEBUGGING, OCTETS
 31 .debug_line_str 00000103  0000000000000000  0000000000000000  0000331a  2**0
                  CONTENTS, READONLY, DEBUGGING, OCTETS
SYMBOL TABLE:
0000000000010270 l    d  .interp	0000000000000000              .interp
0000000000010294 l    d  .note.ABI-tag	0000000000000000              .note.ABI-tag
00000000000102b8 l    d  .hash	0000000000000000              .hash
0000000000010360 l    d  .gnu.hash	0000000000000000              .gnu.hash
0000000000010420 l    d  .dynsym	0000000000000000              .dynsym
0000000000010648 l    d  .dynstr	0000000000000000              .dynstr
0000000000010712 l    d  .gnu.version	0000000000000000              .gnu.version
0000000000010740 l    d  .gnu.version_r	0000000000000000              .gnu.version_r
0000000000010770 l    d  .rela.dyn	0000000000000000              .rela.dyn
00000000000107b8 l    d  .rela.plt	0000000000000000              .rela.plt
0000000000010970 l    d  .plt	0000000000000000              .plt
0000000000010ab0 l    d  .text	0000000000000000              .text
0000000000011190 l    d  .rodata	0000000000000000              .rodata
0000000000011370 l    d  .eh_frame_hdr	0000000000000000              .eh_frame_hdr
00000000000113b8 l    d  .eh_frame	0000000000000000              .eh_frame
0000000000012df0 l    d  .preinit_array	0000000000000000              .preinit_array
0000000000012df8 l    d  .init_array	0000000000000000              .init_array
0000000000012e00 l    d  .fini_array	0000000000000000              .fini_array
0000000000012e08 l    d  .dynamic	0000000000000000              .dynamic
0000000000012fe8 l    d  .got	0000000000000000              .got
0000000000012ff0 l    d  .got.plt	0000000000000000              .got.plt
0000000000013090 l    d  .sdata	0000000000000000              .sdata
0000000000013098 l    d  .sbss	0000000000000000              .sbss
00000000000130b8 l    d  .bss	0000000000000000              .bss
0000000000000000 l    d  .comment	0000000000000000              .comment
0000000000000000 l    d  .riscv.attributes	0000000000000000              .riscv.attributes
0000000000000000 l    d  .debug_aranges	0000000000000000              .debug_aranges
0000000000000000 l    d  .debug_info	0000000000000000              .debug_info
0000000000000000 l    d  .debug_abbrev	0000000000000000              .debug_abbrev
0000000000000000 l    d  .debug_line	0000000000000000              .debug_line
0000000000000000 l    d  .debug_str	0000000000000000              .debug_str
0000000000000000 l    d  .debug_line_str	0000000000000000              .debug_line_str
0000000000000000 l    df *ABS*	0000000000000000              abi-note.c
0000000000010294 l     O .note.ABI-tag	0000000000000020              __abi_tag
0000000000000000 l    df *ABS*	0000000000000000              start.o
0000000000010ae4 l       .text	0000000000000000              load_gp
0000000000010ae0 l       .text	0000000000000000              __wrap_main
0000000000000000 l    df *ABS*	0000000000000000              init.c
0000000000000000 l    df *ABS*	0000000000000000              static-reloc.c
0000000000000000 l    df *ABS*	0000000000000000              crtstuff.c
0000000000010af4 l     F .text	0000000000000000              deregister_tm_clones
0000000000010b1c l     F .text	0000000000000000              register_tm_clones
0000000000010b58 l     F .text	0000000000000000              __do_global_dtors_aux
00000000000130d0 l     O .bss	0000000000000001              completed.0
0000000000012e00 l     O .fini_array	0000000000000000              __do_global_dtors_aux_fini_array_entry
0000000000010b88 l     F .text	0000000000000000              frame_dummy
0000000000012df8 l     O .init_array	0000000000000000              __frame_dummy_init_array_entry
0000000000000000 l    df *ABS*	0000000000000000              browser.c
0000000000010e54 l     F .text	00000000000000bc              hash_djb2
0000000000000000 l    df *ABS*	0000000000000000              crtstuff.c
00000000000114c0 l     O .eh_frame	0000000000000000              __FRAME_END__
0000000000010970 l     O .plt	0000000000000000              _PROCEDURE_LINKAGE_TABLE_
0000000000012e08 l     O .dynamic	0000000000000000              _DYNAMIC
0000000000011370 l       .eh_frame_hdr	0000000000000000              __GNU_EH_FRAME_HDR
0000000000012fe8 l     O .got	0000000000000000              _GLOBAL_OFFSET_TABLE_
0000000000010990       F *UND*	0000000000000000              fprintf@GLIBC_2.27
00000000000130b8 g     O .bss	0000000000000008              stdin@GLIBC_2.27
00000000000109a0       F *UND*	0000000000000000              __libc_start_main@GLIBC_2.34
0000000000013090  w      .sdata	0000000000000000              data_start
00000000000109b0       F *UND*	0000000000000000              getenv@GLIBC_2.27
00000000000109c0       F *UND*	0000000000000000              fwrite@GLIBC_2.27
00000000000109d0       F *UND*	0000000000000000              strdup@GLIBC_2.27
0000000000013148 g       .bss	0000000000000000              __BSS_END__
00000000000109e0       F *UND*	0000000000000000              isatty@GLIBC_2.27
0000000000013098 g       .sdata	0000000000000000              _edata
0000000000013090 g       .sdata	0000000000000000              __SDATA_BEGIN__
0000000000013090 g       .sdata	0000000000000000              __DATA_BEGIN__
00000000000109f0       F *UND*	0000000000000000              fflush@GLIBC_2.27
0000000000010a00       F *UND*	0000000000000000              memcpy@GLIBC_2.27
0000000000010a10       F *UND*	0000000000000000              strlen@GLIBC_2.27
0000000000010a20       F *UND*	0000000000000000              strcpy@GLIBC_2.27
0000000000013090 g       .sdata	0000000000000000              __data_start
0000000000010bc8 g     F .text	00000000000001a4              do_prompt
0000000000013090 g     O .sdata	0000000000000000              .hidden __dso_handle
0000000000011190 g     O .rodata	0000000000000004              _IO_stdin_used
0000000000010a30       F *UND*	0000000000000000              getc@GLIBC_2.27
0000000000013110 g     O .bss	0000000000000032              Net_ID
0000000000013148 g       .bss	0000000000000000              _end
0000000000010af0 g     F .text	0000000000000004              .hidden _dl_relocate_static_pie
0000000000010ab0 g     F .text	0000000000000030              _start
0000000000010b8c g     F .text	000000000000003c              do_response
0000000000013890 g       *ABS*	0000000000000000              __global_pointer$
0000000000013098 g       .sbss	0000000000000000              __bss_start
0000000000010f10 g     F .text	000000000000027c              main
0000000000010d6c g     F .text	00000000000000e8              getuser
0000000000010a40       F *UND*	0000000000000000              exit@GLIBC_2.27
0000000000010a50       F *UND*	0000000000000000              printf@GLIBC_2.27
00000000000130c0 g     O .bss	0000000000000008              stderr@GLIBC_2.27
0000000000010a60       F *UND*	0000000000000000              calloc@GLIBC_2.27
00000000000130d8 g     O .bss	0000000000000032              URL_Buffer
00000000000130a0 g     O .sbss	0000000000000008              heap_url
00000000000130a8 g     O .sbss	0000000000000008              mem_addr
0000000000013090 g     O .sdata	0000000000000000              .hidden __TMC_END__
0000000000010a70       F *UND*	0000000000000000              atoi@GLIBC_2.27
00000000000130b0 g     O .sbss	0000000000000008              url_idx
0000000000013098 g     O .sbss	0000000000000008              Tty_Mod
0000000000010a80       F *UND*	0000000000000000              strcmp@GLIBC_2.27
00000000000130c8 g     O .bss	0000000000000008              stdout@GLIBC_2.27
0000000000010a90       F *UND*	0000000000000000              __ctype_b_loc@GLIBC_2.27
0000000000010aa0       F *UND*	0000000000000000              free@GLIBC_2.27



Disassembly of section .plt:

0000000000010970 <_PROCEDURE_LINKAGE_TABLE_>:
   10970:	97 23 00 00 33 03 c3 41 03 be 03 68 13 03 43 fd     .#..3..A...h..C.
   10980:	93 82 03 68 13 53 13 00 83 b2 82 00 67 00 0e 00     ...h.S......g...

0000000000010990 <fprintf@plt>:
   10990:	00002e17          	auipc	t3,0x2
   10994:	670e3e03          	ld	t3,1648(t3) # 13000 <fprintf@GLIBC_2.27>
   10998:	000e0367          	jalr	t1,t3
   1099c:	00000013          	nop

00000000000109a0 <__libc_start_main@plt>:
   109a0:	00002e17          	auipc	t3,0x2
   109a4:	668e3e03          	ld	t3,1640(t3) # 13008 <__libc_start_main@GLIBC_2.34>
   109a8:	000e0367          	jalr	t1,t3
   109ac:	00000013          	nop

00000000000109b0 <getenv@plt>:
   109b0:	00002e17          	auipc	t3,0x2
   109b4:	660e3e03          	ld	t3,1632(t3) # 13010 <getenv@GLIBC_2.27>
   109b8:	000e0367          	jalr	t1,t3
   109bc:	00000013          	nop

00000000000109c0 <fwrite@plt>:
   109c0:	00002e17          	auipc	t3,0x2
   109c4:	658e3e03          	ld	t3,1624(t3) # 13018 <fwrite@GLIBC_2.27>
   109c8:	000e0367          	jalr	t1,t3
   109cc:	00000013          	nop

00000000000109d0 <strdup@plt>:
   109d0:	00002e17          	auipc	t3,0x2
   109d4:	650e3e03          	ld	t3,1616(t3) # 13020 <strdup@GLIBC_2.27>
   109d8:	000e0367          	jalr	t1,t3
   109dc:	00000013          	nop

00000000000109e0 <isatty@plt>:
   109e0:	00002e17          	auipc	t3,0x2
   109e4:	648e3e03          	ld	t3,1608(t3) # 13028 <isatty@GLIBC_2.27>
   109e8:	000e0367          	jalr	t1,t3
   109ec:	00000013          	nop

00000000000109f0 <fflush@plt>:
   109f0:	00002e17          	auipc	t3,0x2
   109f4:	640e3e03          	ld	t3,1600(t3) # 13030 <fflush@GLIBC_2.27>
   109f8:	000e0367          	jalr	t1,t3
   109fc:	00000013          	nop

0000000000010a00 <memcpy@plt>:
   10a00:	00002e17          	auipc	t3,0x2
   10a04:	638e3e03          	ld	t3,1592(t3) # 13038 <memcpy@GLIBC_2.27>
   10a08:	000e0367          	jalr	t1,t3
   10a0c:	00000013          	nop

0000000000010a10 <strlen@plt>:
   10a10:	00002e17          	auipc	t3,0x2
   10a14:	630e3e03          	ld	t3,1584(t3) # 13040 <strlen@GLIBC_2.27>
   10a18:	000e0367          	jalr	t1,t3
   10a1c:	00000013          	nop

0000000000010a20 <strcpy@plt>:
   10a20:	00002e17          	auipc	t3,0x2
   10a24:	628e3e03          	ld	t3,1576(t3) # 13048 <strcpy@GLIBC_2.27>
   10a28:	000e0367          	jalr	t1,t3
   10a2c:	00000013          	nop

0000000000010a30 <getc@plt>:
   10a30:	00002e17          	auipc	t3,0x2
   10a34:	620e3e03          	ld	t3,1568(t3) # 13050 <getc@GLIBC_2.27>
   10a38:	000e0367          	jalr	t1,t3
   10a3c:	00000013          	nop

0000000000010a40 <exit@plt>:
   10a40:	00002e17          	auipc	t3,0x2
   10a44:	618e3e03          	ld	t3,1560(t3) # 13058 <exit@GLIBC_2.27>
   10a48:	000e0367          	jalr	t1,t3
   10a4c:	00000013          	nop

0000000000010a50 <printf@plt>:
   10a50:	00002e17          	auipc	t3,0x2
   10a54:	610e3e03          	ld	t3,1552(t3) # 13060 <printf@GLIBC_2.27>
   10a58:	000e0367          	jalr	t1,t3
   10a5c:	00000013          	nop

0000000000010a60 <calloc@plt>:
   10a60:	00002e17          	auipc	t3,0x2
   10a64:	608e3e03          	ld	t3,1544(t3) # 13068 <calloc@GLIBC_2.27>
   10a68:	000e0367          	jalr	t1,t3
   10a6c:	00000013          	nop

0000000000010a70 <atoi@plt>:
   10a70:	00002e17          	auipc	t3,0x2
   10a74:	600e3e03          	ld	t3,1536(t3) # 13070 <atoi@GLIBC_2.27>
   10a78:	000e0367          	jalr	t1,t3
   10a7c:	00000013          	nop

0000000000010a80 <strcmp@plt>:
   10a80:	00002e17          	auipc	t3,0x2
   10a84:	5f8e3e03          	ld	t3,1528(t3) # 13078 <strcmp@GLIBC_2.27>
   10a88:	000e0367          	jalr	t1,t3
   10a8c:	00000013          	nop

0000000000010a90 <__ctype_b_loc@plt>:
   10a90:	00002e17          	auipc	t3,0x2
   10a94:	5f0e3e03          	ld	t3,1520(t3) # 13080 <__ctype_b_loc@GLIBC_2.27>
   10a98:	000e0367          	jalr	t1,t3
   10a9c:	00000013          	nop

0000000000010aa0 <free@plt>:
   10aa0:	00002e17          	auipc	t3,0x2
   10aa4:	5e8e3e03          	ld	t3,1512(t3) # 13088 <free@GLIBC_2.27>
   10aa8:	000e0367          	jalr	t1,t3
   10aac:	00000013          	nop

Disassembly of section .text:

0000000000010ab0 <_start>:
_start():
   10ab0:	034000ef          	jal	10ae4 <load_gp>
   10ab4:	00050793          	mv	a5,a0
   10ab8:	00000517          	auipc	a0,0x0
   10abc:	02850513          	addi	a0,a0,40 # 10ae0 <__wrap_main>
   10ac0:	00013583          	ld	a1,0(sp)
   10ac4:	00810613          	addi	a2,sp,8
   10ac8:	ff017113          	andi	sp,sp,-16
   10acc:	00000693          	li	a3,0
   10ad0:	00000713          	li	a4,0
   10ad4:	00010813          	mv	a6,sp
   10ad8:	ec9ff0ef          	jal	109a0 <__libc_start_main@plt>
   10adc:	00100073          	ebreak

0000000000010ae0 <__wrap_main>:
__wrap_main():
   10ae0:	4300006f          	j	10f10 <main>

0000000000010ae4 <load_gp>:
load_gp():
   10ae4:	00003197          	auipc	gp,0x3
   10ae8:	dac18193          	addi	gp,gp,-596 # 13890 <__global_pointer$>
   10aec:	00008067          	ret

0000000000010af0 <_dl_relocate_static_pie>:
_dl_relocate_static_pie():
   10af0:	00008067          	ret

0000000000010af4 <deregister_tm_clones>:
deregister_tm_clones():
   10af4:	00013537          	lui	a0,0x13
   10af8:	00013737          	lui	a4,0x13
   10afc:	09050793          	addi	a5,a0,144 # 13090 <__TMC_END__>
   10b00:	09070713          	addi	a4,a4,144 # 13090 <__TMC_END__>
   10b04:	00f70a63          	beq	a4,a5,10b18 <deregister_tm_clones+0x24>
   10b08:	00000793          	li	a5,0
   10b0c:	00078663          	beqz	a5,10b18 <deregister_tm_clones+0x24>
   10b10:	09050513          	addi	a0,a0,144
   10b14:	00078067          	jr	a5
   10b18:	00008067          	ret

0000000000010b1c <register_tm_clones>:
register_tm_clones():
   10b1c:	00013537          	lui	a0,0x13
   10b20:	09050793          	addi	a5,a0,144 # 13090 <__TMC_END__>
   10b24:	00013737          	lui	a4,0x13
   10b28:	09070593          	addi	a1,a4,144 # 13090 <__TMC_END__>
   10b2c:	40f585b3          	sub	a1,a1,a5
   10b30:	4035d793          	srai	a5,a1,0x3
   10b34:	03f5d593          	srli	a1,a1,0x3f
   10b38:	00f585b3          	add	a1,a1,a5
   10b3c:	4015d593          	srai	a1,a1,0x1
   10b40:	00058a63          	beqz	a1,10b54 <register_tm_clones+0x38>
   10b44:	00000793          	li	a5,0
   10b48:	00078663          	beqz	a5,10b54 <register_tm_clones+0x38>
   10b4c:	09050513          	addi	a0,a0,144
   10b50:	00078067          	jr	a5
   10b54:	00008067          	ret

0000000000010b58 <__do_global_dtors_aux>:
__do_global_dtors_aux():
   10b58:	ff010113          	addi	sp,sp,-16
   10b5c:	00813023          	sd	s0,0(sp)
   10b60:	8401c783          	lbu	a5,-1984(gp) # 130d0 <completed.0>
   10b64:	00113423          	sd	ra,8(sp)
   10b68:	00079863          	bnez	a5,10b78 <__do_global_dtors_aux+0x20>
   10b6c:	f89ff0ef          	jal	10af4 <deregister_tm_clones>
   10b70:	00100793          	li	a5,1
   10b74:	84f18023          	sb	a5,-1984(gp) # 130d0 <completed.0>
   10b78:	00813083          	ld	ra,8(sp)
   10b7c:	00013403          	ld	s0,0(sp)
   10b80:	01010113          	addi	sp,sp,16
   10b84:	00008067          	ret

0000000000010b88 <frame_dummy>:
frame_dummy():
   10b88:	f95ff06f          	j	10b1c <register_tm_clones>

0000000000010b8c <do_response>:
do_response():
/root/browser.c:19
   10b8c:	fe010113          	addi	sp,sp,-32
   10b90:	00113c23          	sd	ra,24(sp)
   10b94:	00813823          	sd	s0,16(sp)
   10b98:	02010413          	addi	s0,sp,32
   10b9c:	fea43423          	sd	a0,-24(s0)
/root/browser.c:20
   10ba0:	000137b7          	lui	a5,0x13
   10ba4:	0987b703          	ld	a4,152(a5) # 13098 <Tty_Mod>
   10ba8:	fe843683          	ld	a3,-24(s0)
   10bac:	88018613          	addi	a2,gp,-1920 # 13110 <Net_ID>
   10bb0:	00070593          	mv	a1,a4
   10bb4:	000117b7          	lui	a5,0x11
   10bb8:	19878513          	addi	a0,a5,408 # 11198 <_IO_stdin_used+0x8>
   10bbc:	e95ff0ef          	jal	10a50 <printf@plt>
/root/browser.c:21
   10bc0:	00000513          	li	a0,0
   10bc4:	e7dff0ef          	jal	10a40 <exit@plt>

0000000000010bc8 <do_prompt>:
do_prompt():
/root/browser.c:27
   10bc8:	fa010113          	addi	sp,sp,-96
   10bcc:	04113c23          	sd	ra,88(sp)
   10bd0:	04813823          	sd	s0,80(sp)
   10bd4:	06010413          	addi	s0,sp,96
   10bd8:	faa43423          	sd	a0,-88(s0)
/root/browser.c:31
   10bdc:	1f400593          	li	a1,500
   10be0:	00100513          	li	a0,1
   10be4:	e7dff0ef          	jal	10a60 <calloc@plt>
   10be8:	00050793          	mv	a5,a0
   10bec:	00078713          	mv	a4,a5
/root/browser.c:31 (discriminator 1)
   10bf0:	80e1b823          	sd	a4,-2032(gp) # 130a0 <heap_url>
/root/browser.c:33
   10bf4:	000117b7          	lui	a5,0x11
   10bf8:	1b878513          	addi	a0,a5,440 # 111b8 <_IO_stdin_used+0x28>
   10bfc:	e55ff0ef          	jal	10a50 <printf@plt>
/root/browser.c:34
   10c00:	8381b783          	ld	a5,-1992(gp) # 130c8 <stdout@GLIBC_2.27>
   10c04:	00078513          	mv	a0,a5
   10c08:	de9ff0ef          	jal	109f0 <fflush@plt>
/root/browser.c:35
   10c0c:	8201b023          	sd	zero,-2016(gp) # 130b0 <url_idx>
   10c10:	04c0006f          	j	10c5c <do_prompt+0x94>
/root/browser.c:36
   10c14:	8281b783          	ld	a5,-2008(gp) # 130b8 <stdin@GLIBC_2.27>
   10c18:	00078513          	mv	a0,a5
   10c1c:	e15ff0ef          	jal	10a30 <getc@plt>
   10c20:	00050793          	mv	a5,a0
   10c24:	fef42423          	sw	a5,-24(s0)
/root/browser.c:37
   10c28:	8101b703          	ld	a4,-2032(gp) # 130a0 <heap_url>
   10c2c:	8201b783          	ld	a5,-2016(gp) # 130b0 <url_idx>
   10c30:	00f707b3          	add	a5,a4,a5
   10c34:	fe842703          	lw	a4,-24(s0)
   10c38:	0ff77713          	zext.b	a4,a4
   10c3c:	00e78023          	sb	a4,0(a5)
/root/browser.c:38
   10c40:	fe842783          	lw	a5,-24(s0)
   10c44:	0007871b          	sext.w	a4,a5
   10c48:	00a00793          	li	a5,10
   10c4c:	02f70263          	beq	a4,a5,10c70 <do_prompt+0xa8>
/root/browser.c:35 (discriminator 2)
   10c50:	8201b783          	ld	a5,-2016(gp) # 130b0 <url_idx>
   10c54:	00178713          	addi	a4,a5,1
   10c58:	82e1b023          	sd	a4,-2016(gp) # 130b0 <url_idx>
/root/browser.c:35 (discriminator 1)
   10c5c:	fa843783          	ld	a5,-88(s0)
   10c60:	fff78713          	addi	a4,a5,-1
   10c64:	8201b783          	ld	a5,-2016(gp) # 130b0 <url_idx>
   10c68:	fae7e6e3          	bltu	a5,a4,10c14 <do_prompt+0x4c>
   10c6c:	0080006f          	j	10c74 <do_prompt+0xac>
/root/browser.c:39
   10c70:	00000013          	nop
/root/browser.c:41
   10c74:	8001bc23          	sd	zero,-2024(gp) # 130a8 <mem_addr>
   10c78:	0300006f          	j	10ca8 <do_prompt+0xe0>
/root/browser.c:42
   10c7c:	8101b703          	ld	a4,-2032(gp) # 130a0 <heap_url>
   10c80:	8181b783          	ld	a5,-2024(gp) # 130a8 <mem_addr>
   10c84:	00f70733          	add	a4,a4,a5
   10c88:	8181b783          	ld	a5,-2024(gp) # 130a8 <mem_addr>
   10c8c:	00074703          	lbu	a4,0(a4)
   10c90:	ff078793          	addi	a5,a5,-16
   10c94:	008787b3          	add	a5,a5,s0
   10c98:	fce78023          	sb	a4,-64(a5)
/root/browser.c:41 (discriminator 3)
   10c9c:	8181b783          	ld	a5,-2024(gp) # 130a8 <mem_addr>
   10ca0:	00178713          	addi	a4,a5,1
   10ca4:	80e1bc23          	sd	a4,-2024(gp) # 130a8 <mem_addr>
/root/browser.c:41 (discriminator 1)
   10ca8:	8181b703          	ld	a4,-2024(gp) # 130a8 <mem_addr>
   10cac:	8201b783          	ld	a5,-2016(gp) # 130b0 <url_idx>
   10cb0:	fcf766e3          	bltu	a4,a5,10c7c <do_prompt+0xb4>
/root/browser.c:44
   10cb4:	8101b783          	ld	a5,-2032(gp) # 130a0 <heap_url>
   10cb8:	00078513          	mv	a0,a5
   10cbc:	de5ff0ef          	jal	10aa0 <free@plt>
/root/browser.c:45
   10cc0:	8201b783          	ld	a5,-2016(gp) # 130b0 <url_idx>
   10cc4:	ff078793          	addi	a5,a5,-16
   10cc8:	008787b3          	add	a5,a5,s0
   10ccc:	fc078023          	sb	zero,-64(a5)
/root/browser.c:47
   10cd0:	fe042623          	sw	zero,-20(s0)
/root/browser.c:48
   10cd4:	0100006f          	j	10ce4 <do_prompt+0x11c>
/root/browser.c:49
   10cd8:	fec42783          	lw	a5,-20(s0)
   10cdc:	0017879b          	addiw	a5,a5,1
   10ce0:	fef42623          	sw	a5,-20(s0)
/root/browser.c:48
   10ce4:	dadff0ef          	jal	10a90 <__ctype_b_loc@plt>
   10ce8:	00050793          	mv	a5,a0
/root/browser.c:48 (discriminator 1)
   10cec:	0007b703          	ld	a4,0(a5)
   10cf0:	fec42783          	lw	a5,-20(s0)
   10cf4:	ff078793          	addi	a5,a5,-16
   10cf8:	008787b3          	add	a5,a5,s0
   10cfc:	fc07c783          	lbu	a5,-64(a5)
   10d00:	00179793          	slli	a5,a5,0x1
   10d04:	00f707b3          	add	a5,a4,a5
   10d08:	0007d783          	lhu	a5,0(a5)
   10d0c:	0007871b          	sext.w	a4,a5
   10d10:	000027b7          	lui	a5,0x2
   10d14:	00f777b3          	and	a5,a4,a5
   10d18:	0007879b          	sext.w	a5,a5
   10d1c:	00079c63          	bnez	a5,10d34 <do_prompt+0x16c>
   10d20:	fec42783          	lw	a5,-20(s0)
   10d24:	ff078793          	addi	a5,a5,-16 # 1ff0 <__abi_tag-0xe2a4>
   10d28:	008787b3          	add	a5,a5,s0
   10d2c:	fc07c783          	lbu	a5,-64(a5)
   10d30:	fa0794e3          	bnez	a5,10cd8 <do_prompt+0x110>
/root/browser.c:51
   10d34:	fec42783          	lw	a5,-20(s0)
   10d38:	ff078793          	addi	a5,a5,-16
   10d3c:	008787b3          	add	a5,a5,s0
   10d40:	fc078023          	sb	zero,-64(a5)
/root/browser.c:52
   10d44:	fb040793          	addi	a5,s0,-80
   10d48:	00078593          	mv	a1,a5
   10d4c:	84818513          	addi	a0,gp,-1976 # 130d8 <URL_Buffer>
   10d50:	cd1ff0ef          	jal	10a20 <strcpy@plt>
/root/browser.c:53
   10d54:	84818793          	addi	a5,gp,-1976 # 130d8 <URL_Buffer>
/root/browser.c:54
   10d58:	00078513          	mv	a0,a5
   10d5c:	05813083          	ld	ra,88(sp)
   10d60:	05013403          	ld	s0,80(sp)
   10d64:	06010113          	addi	sp,sp,96
   10d68:	00008067          	ret

0000000000010d6c <getuser>:
getuser():
/root/browser.c:56
   10d6c:	fd010113          	addi	sp,sp,-48
   10d70:	02113423          	sd	ra,40(sp)
   10d74:	02813023          	sd	s0,32(sp)
   10d78:	03010413          	addi	s0,sp,48
   10d7c:	fca43c23          	sd	a0,-40(s0)
   10d80:	00058793          	mv	a5,a1
   10d84:	fcf42a23          	sw	a5,-44(s0)
/root/browser.c:57
   10d88:	000117b7          	lui	a5,0x11
   10d8c:	1d078513          	addi	a0,a5,464 # 111d0 <_IO_stdin_used+0x40>
   10d90:	c21ff0ef          	jal	109b0 <getenv@plt>
   10d94:	fea43423          	sd	a0,-24(s0)
/root/browser.c:58
   10d98:	fe843783          	ld	a5,-24(s0)
   10d9c:	02079463          	bnez	a5,10dc4 <getuser+0x58>
/root/browser.c:59
   10da0:	8301b783          	ld	a5,-2000(gp) # 130c0 <stderr@GLIBC_2.27>
   10da4:	00078693          	mv	a3,a5
   10da8:	03400613          	li	a2,52
   10dac:	00100593          	li	a1,1
   10db0:	000117b7          	lui	a5,0x11
   10db4:	1d878513          	addi	a0,a5,472 # 111d8 <_IO_stdin_used+0x48>
   10db8:	c09ff0ef          	jal	109c0 <fwrite@plt>
/root/browser.c:60
   10dbc:	00100513          	li	a0,1
   10dc0:	c81ff0ef          	jal	10a40 <exit@plt>
/root/browser.c:63
   10dc4:	fe843503          	ld	a0,-24(s0)
   10dc8:	c09ff0ef          	jal	109d0 <strdup@plt>
   10dcc:	00050793          	mv	a5,a0
   10dd0:	fef43423          	sd	a5,-24(s0)
/root/browser.c:64
   10dd4:	fe843503          	ld	a0,-24(s0)
   10dd8:	c39ff0ef          	jal	10a10 <strlen@plt>
   10ddc:	00050793          	mv	a5,a0
/root/browser.c:64 (discriminator 1)
   10de0:	00178713          	addi	a4,a5,1
   10de4:	fd442783          	lw	a5,-44(s0)
   10de8:	00e7fc63          	bgeu	a5,a4,10e00 <getuser+0x94>
/root/browser.c:65
   10dec:	fd442783          	lw	a5,-44(s0)
   10df0:	fff78793          	addi	a5,a5,-1
   10df4:	fe843703          	ld	a4,-24(s0)
   10df8:	00f707b3          	add	a5,a4,a5
   10dfc:	00078023          	sb	zero,0(a5)
/root/browser.c:66
   10e00:	fe843503          	ld	a0,-24(s0)
   10e04:	c0dff0ef          	jal	10a10 <strlen@plt>
   10e08:	00050793          	mv	a5,a0
/root/browser.c:66 (discriminator 1)
   10e0c:	00178793          	addi	a5,a5,1
   10e10:	00078613          	mv	a2,a5
   10e14:	fe843583          	ld	a1,-24(s0)
   10e18:	fd843503          	ld	a0,-40(s0)
   10e1c:	be5ff0ef          	jal	10a00 <memcpy@plt>
/root/browser.c:68
   10e20:	8301b703          	ld	a4,-2000(gp) # 130c0 <stderr@GLIBC_2.27>
   10e24:	fd843603          	ld	a2,-40(s0)
   10e28:	000117b7          	lui	a5,0x11
   10e2c:	21078593          	addi	a1,a5,528 # 11210 <_IO_stdin_used+0x80>
   10e30:	00070513          	mv	a0,a4
   10e34:	b5dff0ef          	jal	10990 <fprintf@plt>
/root/browser.c:69
   10e38:	fe843503          	ld	a0,-24(s0)
   10e3c:	c65ff0ef          	jal	10aa0 <free@plt>
/root/browser.c:70
   10e40:	00000013          	nop
   10e44:	02813083          	ld	ra,40(sp)
   10e48:	02013403          	ld	s0,32(sp)
   10e4c:	03010113          	addi	sp,sp,48
   10e50:	00008067          	ret

0000000000010e54 <hash_djb2>:
hash_djb2():
/root/browser.c:72
   10e54:	fc010113          	addi	sp,sp,-64
   10e58:	02113c23          	sd	ra,56(sp)
   10e5c:	02813823          	sd	s0,48(sp)
   10e60:	04010413          	addi	s0,sp,64
   10e64:	fca43423          	sd	a0,-56(s0)
/root/browser.c:73
   10e68:	000017b7          	lui	a5,0x1
   10e6c:	50578793          	addi	a5,a5,1285 # 1505 <__abi_tag-0xed8f>
   10e70:	fef43423          	sd	a5,-24(s0)
/root/browser.c:74
   10e74:	fc843503          	ld	a0,-56(s0)
   10e78:	b99ff0ef          	jal	10a10 <strlen@plt>
   10e7c:	fea43023          	sd	a0,-32(s0)
/root/browser.c:76
   10e80:	fe043783          	ld	a5,-32(s0)
   10e84:	04078c63          	beqz	a5,10edc <hash_djb2+0x88>
/root/browser.c:76 (discriminator 1)
   10e88:	fe043783          	ld	a5,-32(s0)
   10e8c:	fff78793          	addi	a5,a5,-1
   10e90:	fc843703          	ld	a4,-56(s0)
   10e94:	00f707b3          	add	a5,a4,a5
   10e98:	0007c783          	lbu	a5,0(a5)
   10e9c:	00078713          	mv	a4,a5
   10ea0:	00a00793          	li	a5,10
   10ea4:	02f71c63          	bne	a4,a5,10edc <hash_djb2+0x88>
/root/browser.c:77
   10ea8:	fe043783          	ld	a5,-32(s0)
   10eac:	fff78793          	addi	a5,a5,-1
   10eb0:	fc843703          	ld	a4,-56(s0)
   10eb4:	00f707b3          	add	a5,a4,a5
   10eb8:	00078023          	sb	zero,0(a5)
/root/browser.c:81
   10ebc:	0200006f          	j	10edc <hash_djb2+0x88>
/root/browser.c:82
   10ec0:	fe843783          	ld	a5,-24(s0)
   10ec4:	00579713          	slli	a4,a5,0x5
   10ec8:	fe843783          	ld	a5,-24(s0)
   10ecc:	00f707b3          	add	a5,a4,a5
   10ed0:	fd843703          	ld	a4,-40(s0)
   10ed4:	00f707b3          	add	a5,a4,a5
   10ed8:	fef43423          	sd	a5,-24(s0)
/root/browser.c:81
   10edc:	fc843783          	ld	a5,-56(s0)
   10ee0:	00178713          	addi	a4,a5,1
   10ee4:	fce43423          	sd	a4,-56(s0)
   10ee8:	0007c783          	lbu	a5,0(a5)
   10eec:	fcf43c23          	sd	a5,-40(s0)
   10ef0:	fd843783          	ld	a5,-40(s0)
   10ef4:	fc0796e3          	bnez	a5,10ec0 <hash_djb2+0x6c>
/root/browser.c:85
   10ef8:	fe843783          	ld	a5,-24(s0)
/root/browser.c:86
   10efc:	00078513          	mv	a0,a5
   10f00:	03813083          	ld	ra,56(sp)
   10f04:	03013403          	ld	s0,48(sp)
   10f08:	04010113          	addi	sp,sp,64
   10f0c:	00008067          	ret

0000000000010f10 <main>:
main():
/root/browser.c:88
   10f10:	fb010113          	addi	sp,sp,-80
   10f14:	04113423          	sd	ra,72(sp)
   10f18:	04813023          	sd	s0,64(sp)
   10f1c:	05010413          	addi	s0,sp,80
   10f20:	00050793          	mv	a5,a0
   10f24:	fab43823          	sd	a1,-80(s0)
   10f28:	faf42e23          	sw	a5,-68(s0)
/root/browser.c:90
   10f2c:	fbc42783          	lw	a5,-68(s0)
   10f30:	0007871b          	sext.w	a4,a5
   10f34:	00200793          	li	a5,2
   10f38:	00f71c63          	bne	a4,a5,10f50 <main+0x40>
/root/browser.c:90 (discriminator 1)
   10f3c:	fb043783          	ld	a5,-80(s0)
   10f40:	00878793          	addi	a5,a5,8
   10f44:	0007b783          	ld	a5,0(a5)
   10f48:	0007c783          	lbu	a5,0(a5)
   10f4c:	02079463          	bnez	a5,10f74 <main+0x64>
/root/browser.c:91
   10f50:	8301b783          	ld	a5,-2000(gp) # 130c0 <stderr@GLIBC_2.27>
   10f54:	00078693          	mv	a3,a5
   10f58:	04900613          	li	a2,73
   10f5c:	00100593          	li	a1,1
   10f60:	000117b7          	lui	a5,0x11
   10f64:	22878513          	addi	a0,a5,552 # 11228 <_IO_stdin_used+0x98>
   10f68:	a59ff0ef          	jal	109c0 <fwrite@plt>
/root/browser.c:93
   10f6c:	00100513          	li	a0,1
   10f70:	ad1ff0ef          	jal	10a40 <exit@plt>
/root/browser.c:96
   10f74:	fb043783          	ld	a5,-80(s0)
   10f78:	0087b783          	ld	a5,8(a5)
   10f7c:	fef43023          	sd	a5,-32(s0)
/root/browser.c:97
   10f80:	fe043423          	sd	zero,-24(s0)
   10f84:	0a40006f          	j	11028 <main+0x118>
/root/browser.c:99
   10f88:	b09ff0ef          	jal	10a90 <__ctype_b_loc@plt>
   10f8c:	00050793          	mv	a5,a0
/root/browser.c:99 (discriminator 1)
   10f90:	0007b703          	ld	a4,0(a5)
   10f94:	fe043683          	ld	a3,-32(s0)
   10f98:	fe843783          	ld	a5,-24(s0)
   10f9c:	00f687b3          	add	a5,a3,a5
   10fa0:	0007c783          	lbu	a5,0(a5)
   10fa4:	00179793          	slli	a5,a5,0x1
   10fa8:	00f707b3          	add	a5,a4,a5
   10fac:	0007d783          	lhu	a5,0(a5)
   10fb0:	0007871b          	sext.w	a4,a5
   10fb4:	000017b7          	lui	a5,0x1
   10fb8:	80078793          	addi	a5,a5,-2048 # 800 <__abi_tag-0xfa94>
   10fbc:	00f777b3          	and	a5,a4,a5
   10fc0:	0007879b          	sext.w	a5,a5
   10fc4:	02079463          	bnez	a5,10fec <main+0xdc>
/root/browser.c:100
   10fc8:	8301b783          	ld	a5,-2000(gp) # 130c0 <stderr@GLIBC_2.27>
   10fcc:	00078693          	mv	a3,a5
   10fd0:	02d00613          	li	a2,45
   10fd4:	00100593          	li	a1,1
   10fd8:	000117b7          	lui	a5,0x11
   10fdc:	27878513          	addi	a0,a5,632 # 11278 <_IO_stdin_used+0xe8>
   10fe0:	9e1ff0ef          	jal	109c0 <fwrite@plt>
/root/browser.c:101
   10fe4:	00100513          	li	a0,1
   10fe8:	a59ff0ef          	jal	10a40 <exit@plt>
/root/browser.c:103
   10fec:	fe843703          	ld	a4,-24(s0)
   10ff0:	00100793          	li	a5,1
   10ff4:	02e7f463          	bgeu	a5,a4,1101c <main+0x10c>
/root/browser.c:104
   10ff8:	8301b783          	ld	a5,-2000(gp) # 130c0 <stderr@GLIBC_2.27>
   10ffc:	00078693          	mv	a3,a5
   11000:	03200613          	li	a2,50
   11004:	00100593          	li	a1,1
   11008:	000117b7          	lui	a5,0x11
   1100c:	2a878513          	addi	a0,a5,680 # 112a8 <_IO_stdin_used+0x118>
   11010:	9b1ff0ef          	jal	109c0 <fwrite@plt>
/root/browser.c:105
   11014:	00100513          	li	a0,1
   11018:	a29ff0ef          	jal	10a40 <exit@plt>
/root/browser.c:98
   1101c:	fe843783          	ld	a5,-24(s0)
   11020:	00178793          	addi	a5,a5,1
   11024:	fef43423          	sd	a5,-24(s0)
/root/browser.c:97 (discriminator 1)
   11028:	fe043703          	ld	a4,-32(s0)
   1102c:	fe843783          	ld	a5,-24(s0)
   11030:	00f707b3          	add	a5,a4,a5
   11034:	0007c783          	lbu	a5,0(a5)
   11038:	f40798e3          	bnez	a5,10f88 <main+0x78>
/root/browser.c:110
   1103c:	fe043503          	ld	a0,-32(s0)
   11040:	a31ff0ef          	jal	10a70 <atoi@plt>
   11044:	00050793          	mv	a5,a0
   11048:	00078713          	mv	a4,a5
/root/browser.c:110 (discriminator 1)
   1104c:	03100793          	li	a5,49
   11050:	00e7ca63          	blt	a5,a4,11064 <main+0x154>
   11054:	fe043503          	ld	a0,-32(s0)
   11058:	a19ff0ef          	jal	10a70 <atoi@plt>
   1105c:	00050793          	mv	a5,a0
   11060:	0080006f          	j	11068 <main+0x158>
/root/browser.c:110 (discriminator 2)
   11064:	03100793          	li	a5,49
/root/browser.c:109
   11068:	fcf42e23          	sw	a5,-36(s0)
/root/browser.c:112
   1106c:	00000513          	li	a0,0
   11070:	971ff0ef          	jal	109e0 <isatty@plt>
   11074:	00050793          	mv	a5,a0
/root/browser.c:112 (discriminator 1)
   11078:	00078863          	beqz	a5,11088 <main+0x178>
   1107c:	000117b7          	lui	a5,0x11
   11080:	2e078793          	addi	a5,a5,736 # 112e0 <_IO_stdin_used+0x150>
   11084:	00c0006f          	j	11090 <main+0x180>
/root/browser.c:112 (discriminator 2)
   11088:	000117b7          	lui	a5,0x11
   1108c:	2e878793          	addi	a5,a5,744 # 112e8 <_IO_stdin_used+0x158>
/root/browser.c:112 (discriminator 4)
   11090:	00013737          	lui	a4,0x13
   11094:	08f73c23          	sd	a5,152(a4) # 13098 <Tty_Mod>
/root/browser.c:113
   11098:	03200593          	li	a1,50
   1109c:	88018513          	addi	a0,gp,-1920 # 13110 <Net_ID>
   110a0:	ccdff0ef          	jal	10d6c <getuser>
/root/browser.c:117
   110a4:	88018513          	addi	a0,gp,-1920 # 13110 <Net_ID>
   110a8:	dadff0ef          	jal	10e54 <hash_djb2>
   110ac:	fca43823          	sd	a0,-48(s0)
/root/browser.c:118
   110b0:	fd043703          	ld	a4,-48(s0)
   110b4:	00375693          	srli	a3,a4,0x3
   110b8:	000117b7          	lui	a5,0x11
   110bc:	3687b783          	ld	a5,872(a5) # 11368 <_IO_stdin_used+0x1d8>
   110c0:	02f6b7b3          	mulhu	a5,a3,a5
   110c4:	0027d793          	srli	a5,a5,0x2
   110c8:	0c800693          	li	a3,200
   110cc:	02d787b3          	mul	a5,a5,a3
   110d0:	40f707b3          	sub	a5,a4,a5
   110d4:	0007879b          	sext.w	a5,a5
   110d8:	0037979b          	slliw	a5,a5,0x3
   110dc:	fcf42623          	sw	a5,-52(s0)
/root/browser.c:124
   110e0:	fcc42783          	lw	a5,-52(s0)
   110e4:	015562b7          	lui	t0,0x1556
   110e8:	d572829b          	addiw	t0,t0,-681 # 1555d57 <__global_pointer$+0x15424c7>
   110ec:	00c29293          	slli	t0,t0,0xc
   110f0:	b2028293          	addi	t0,t0,-1248
   110f4:	40f282b3          	sub	t0,t0,a5
   110f8:	ff828293          	addi	t0,t0,-8
   110fc:	00213023          	sd	sp,0(sp)
   11100:	00028113          	mv	sp,t0
/root/browser.c:133
   11104:	fdc42783          	lw	a5,-36(s0)
   11108:	00078513          	mv	a0,a5
   1110c:	abdff0ef          	jal	10bc8 <do_prompt>
   11110:	fca43023          	sd	a0,-64(s0)
/root/browser.c:138
   11114:	000117b7          	lui	a5,0x11
   11118:	2f078593          	addi	a1,a5,752 # 112f0 <_IO_stdin_used+0x160>
   1111c:	fc043503          	ld	a0,-64(s0)
   11120:	961ff0ef          	jal	10a80 <strcmp@plt>
   11124:	00050793          	mv	a5,a0
/root/browser.c:138 (discriminator 1)
   11128:	04078063          	beqz	a5,11168 <main+0x258>
/root/browser.c:139
   1112c:	000137b7          	lui	a5,0x13
   11130:	0987b703          	ld	a4,152(a5) # 13098 <Tty_Mod>
   11134:	fc043683          	ld	a3,-64(s0)
   11138:	88018613          	addi	a2,gp,-1920 # 13110 <Net_ID>
   1113c:	00070593          	mv	a1,a4
   11140:	000117b7          	lui	a5,0x11
   11144:	30878513          	addi	a0,a5,776 # 11308 <_IO_stdin_used+0x178>
   11148:	909ff0ef          	jal	10a50 <printf@plt>
/root/browser.c:142
   1114c:	000117b7          	lui	a5,0x11
   11150:	2f078593          	addi	a1,a5,752 # 112f0 <_IO_stdin_used+0x160>
   11154:	000117b7          	lui	a5,0x11
   11158:	35078513          	addi	a0,a5,848 # 11350 <_IO_stdin_used+0x1c0>
   1115c:	8f5ff0ef          	jal	10a50 <printf@plt>
/root/browser.c:143
   11160:	00200513          	li	a0,2
   11164:	8ddff0ef          	jal	10a40 <exit@plt>
/root/browser.c:145
   11168:	fc043503          	ld	a0,-64(s0)
   1116c:	a21ff0ef          	jal	10b8c <do_response>
/root/browser.c:147
   11170:	00013103          	ld	sp,0(sp)
/root/browser.c:152
   11174:	00000793          	li	a5,0
/root/browser.c:153
   11178:	00078513          	mv	a0,a5
   1117c:	04813083          	ld	ra,72(sp)
   11180:	04013403          	ld	s0,64(sp)
   11184:	05010113          	addi	sp,sp,80
   11188:	00008067          	ret
