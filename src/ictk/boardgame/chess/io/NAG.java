package ictk.boardgame.chess.io;


import java.util.Locale;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;

public class NAG {
   private static Logger log = Logger.getLogger(NAG.class.getName());
   public static short MAX_NAG = 255;
   protected Locale locale;


   public NAG() {}

   public NAG(Locale locale) {
      this.setLocale(locale);
   }

   public boolean setLocale(Locale locale) {
      return false;
   }

   public Locale getLocale() {
      return null;
   }

   public static short[] stringToNumbers(String s) {
      if(s != null && !s.equals("")) {
         StringTokenizer st = new StringTokenizer(s, " ", false);
         String tok = null;
         short[] nags = (short[])null;
         boolean nag = false;
         short[] rnags = (short[])null;
         if(st.countTokens() > 0) {
            nags = new short[st.countTokens()];
         }

         int count = 0;

         while(st.hasMoreTokens()) {
            tok = st.nextToken();
            short var7 = stringToNumber(tok);
            if(var7 > 0) {
               nags[count++] = var7;
               nag = false;
            }
         }

         if(nags != null && count > 0 && count < nags.length) {
            rnags = new short[count];
            System.arraycopy(nags, 0, rnags, 0, rnags.length);
            nags = rnags;
         }

         if(count == 0) {
            nags = (short[])null;
         }

         return nags;
      } else {
         return null;
      }
   }

   public static short stringToNumber(String str) {
      boolean nag = false;
      short nag1 = symbolToNumber(str);
      if(nag1 == 0 && str.charAt(0) == 36) {
         try {
            nag1 = Short.parseShort(str.substring(1));
         } catch (NumberFormatException var3) {
            log.error("NAG parsed a bad token: " + str.substring(1));
         }
      }

      return nag1;
   }

   public static boolean isSuffix(String str) {
      return isSuffix(symbolToNumber(str));
   }

   public static boolean isSuffix(int s) {
      boolean t = false;
      switch(s) {
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
         t = true;
         break;
      default:
         t = false;
      }

      return t;
   }

   public static boolean isSymbol(int s) {
      boolean t = false;
      switch(s) {
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 10:
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
      case 16:
      case 17:
      case 18:
      case 19:
      case 145:
      case 146:
      case 201:
         t = true;
      default:
         return t;
      }
   }

   public static boolean isSymbol(String str) {
      return symbolToNumber(str) != 0;
   }

   public static short symbolToNumber(String str) {
      short nag = 0;
      if("!".equals(str)) {
         nag = 1;
      } else if("?".equals(str)) {
         nag = 2;
      } else if("!!".equals(str)) {
         nag = 3;
      } else if("??".equals(str)) {
         nag = 4;
      } else if("!?".equals(str)) {
         nag = 5;
      } else if("?!".equals(str)) {
         nag = 6;
      } else if("=".equals(str)) {
         nag = 10;
      } else if("~".equals(str)) {
         nag = 13;
      } else if("+=".equals(str)) {
         nag = 14;
      } else if("=+".equals(str)) {
         nag = 15;
      } else if("+/-".equals(str)) {
         nag = 16;
      } else if("-/+".equals(str)) {
         nag = 17;
      } else if("+-".equals(str)) {
         nag = 18;
      } else if("-+".equals(str)) {
         nag = 19;
      } else if("RR".equals(str)) {
         nag = 145;
      } else if("N".equals(str)) {
         nag = 146;
      } else if("D".equals(str)) {
         nag = 201;
      }

      return nag;
   }

   public static String numberToString(int nag) {
      return numberToString(nag, false);
   }

   public static String numberToString(int nag, boolean allNumeric) {
      String s = null;
      if(allNumeric) {
         if(nag <= MAX_NAG) {
            s = "$" + nag;
         }
      } else {
         switch(nag) {
         case 1:
            s = "!";
            break;
         case 2:
            s = "?";
            break;
         case 3:
            s = "!!";
            break;
         case 4:
            s = "??";
            break;
         case 5:
            s = "!?";
            break;
         case 6:
            s = "?!";
            break;
         case 10:
         case 11:
         case 12:
            s = "=";
            break;
         case 13:
            s = "~";
            break;
         case 14:
            s = "+=";
            break;
         case 15:
            s = "=+";
            break;
         case 16:
            s = "+/-";
            break;
         case 17:
            s = "-/+";
            break;
         case 18:
            s = "+-";
            break;
         case 19:
            s = "-+";
            break;
         case 20:
            s = "+-";
            break;
         case 21:
            s = "-+";
            break;
         case 145:
            s = "RR";
            break;
         case 146:
            s = "N";
            break;
         case 201:
            s = "D";
            break;
         default:
            if(nag <= MAX_NAG) {
               s = "$" + nag;
            }
         }
      }

      return s;
   }

   public String numberToDescription(int nag) {
      String s = null;
      switch(nag) {
      case 1:
         s = "good move";
         break;
      case 2:
         s = "poor move";
         break;
      case 3:
         s = "very good move";
         break;
      case 4:
         s = "very poor move";
         break;
      case 5:
         s = "speculative move";
         break;
      case 6:
         s = "questionable move";
         break;
      case 7:
         s = "forced move (all others lose quickly)";
         break;
      case 8:
         s = "singular move (no reasonable alternatives)";
         break;
      case 9:
         s = "worst move";
         break;
      case 10:
         s = "drawish position";
         break;
      case 11:
         s = "equal chances, quiet position";
         break;
      case 12:
         s = "equal chances, active position";
         break;
      case 13:
         s = "unclear position";
         break;
      case 14:
         s = "White has a slight advantage";
         break;
      case 15:
         s = "Black has a slight advantage";
         break;
      case 16:
         s = "White has a moderate advantage";
         break;
      case 17:
         s = "Black has a moderate advantage";
         break;
      case 18:
         s = "White has a decisive advantage";
         break;
      case 19:
         s = "Black has a decisive advantage";
         break;
      case 20:
         s = "White has a crushing advantage (Black should resign)";
         break;
      case 21:
         s = "Black has a crushing advantage (White should resign)";
         break;
      case 22:
         s = "White is in zugzwang";
         break;
      case 23:
         s = "Black is in zugzwang";
         break;
      case 24:
         s = "White has a slight space advantage";
         break;
      case 25:
         s = "Black has a slight space advantage";
         break;
      case 26:
         s = "White has a moderate space advantage";
         break;
      case 27:
         s = "Black has a moderate space advantage";
         break;
      case 28:
         s = "White has a decisive space advantage";
         break;
      case 29:
         s = "Black has a decisive space advantage";
         break;
      case 30:
         s = "White has a slight time (development) advantage";
         break;
      case 31:
         s = "Black has a slight time (development) advantage";
         break;
      case 32:
         s = "White has a moderate time (development) advantage";
         break;
      case 33:
         s = "Black has a moderate time (development) advantage";
         break;
      case 34:
         s = "White has a decisive time (development) advantage";
         break;
      case 35:
         s = "Black has a decisive time (development) advantage";
         break;
      case 36:
         s = "White has the initiative";
         break;
      case 37:
         s = "Black has the initiative";
         break;
      case 38:
         s = "White has a lasting initiative";
         break;
      case 39:
         s = "Black has a lasting initiative";
         break;
      case 40:
         s = "White has the attack";
         break;
      case 41:
         s = "Black has the attack";
         break;
      case 42:
         s = "White has insufficient compensation for material deficit";
         break;
      case 43:
         s = "Black has insufficient compensation for material deficit";
         break;
      case 44:
         s = "White has sufficient compensation for material deficit";
         break;
      case 45:
         s = "Black has sufficient compensation for material deficit";
         break;
      case 46:
         s = "White has more than adequate compensation for material deficit";
         break;
      case 47:
         s = "Black has more than adequate compensation for material deficit";
         break;
      case 48:
         s = "White has a slight center control advantage";
         break;
      case 49:
         s = "Black has a slight center control advantage";
         break;
      case 50:
         s = "White has a moderate center control advantage";
         break;
      case 51:
         s = "Black has a moderate center control advantage";
         break;
      case 52:
         s = "White has a decisive center control advantage";
         break;
      case 53:
         s = "Black has a decisive center control advantage";
         break;
      case 54:
         s = "White has a slight kingside control advantage";
         break;
      case 55:
         s = "Black has a slight kingside control advantage";
         break;
      case 56:
         s = "White has a moderate kingside control advantage";
         break;
      case 57:
         s = "Black has a moderate kingside control advantage";
         break;
      case 58:
         s = "White has a decisive kingside control advantage";
         break;
      case 59:
         s = "Black has a decisive kingside control advantage";
         break;
      case 60:
         s = "White has a slight queenside control advantage";
         break;
      case 61:
         s = "Black has a slight queenside control advantage";
         break;
      case 62:
         s = "White has a moderate queenside control advantage";
         break;
      case 63:
         s = "Black has a moderate queenside control advantage";
         break;
      case 64:
         s = "White has a decisive queenside control advantage";
         break;
      case 65:
         s = "Black has a decisive queenside control advantage";
         break;
      case 66:
         s = "White has a vulnerable first rank";
         break;
      case 67:
         s = "Black has a vulnerable first rank";
         break;
      case 68:
         s = "White has a well protected first rank";
         break;
      case 69:
         s = "Black has a well protected first rank";
         break;
      case 70:
         s = "White has a poorly protected king";
         break;
      case 71:
         s = "Black has a poorly protected king";
         break;
      case 72:
         s = "White has a well protected king";
         break;
      case 73:
         s = "Black has a well protected king";
         break;
      case 74:
         s = "White has a poorly placed king";
         break;
      case 75:
         s = "Black has a poorly placed king";
         break;
      case 76:
         s = "White has a well placed king";
         break;
      case 77:
         s = "Black has a well placed king";
         break;
      case 78:
         s = "White has a very weak pawn structure";
         break;
      case 79:
         s = "Black has a very weak pawn structure";
         break;
      case 80:
         s = "White has a moderately weak pawn structure";
         break;
      case 81:
         s = "Black has a moderately weak pawn structure";
         break;
      case 82:
         s = "White has a moderately strong pawn structure";
         break;
      case 83:
         s = "Black has a moderately strong pawn structure";
         break;
      case 84:
         s = "White has a very strong pawn structure";
         break;
      case 85:
         s = "Black has a very strong pawn structure";
         break;
      case 86:
         s = "White has poor knight placement";
         break;
      case 87:
         s = "Black has poor knight placement";
         break;
      case 88:
         s = "White has good knight placement";
         break;
      case 89:
         s = "Black has good knight placement";
         break;
      case 90:
         s = "White has poor bishop placement";
         break;
      case 91:
         s = "Black has poor bishop placement";
         break;
      case 92:
         s = "White has good bishop placement";
         break;
      case 93:
         s = "Black has good bishop placement";
         break;
      case 94:
         s = "White has poor rook placement";
         break;
      case 95:
         s = "Black has poor rook placement";
         break;
      case 96:
         s = "White has good rook placement";
         break;
      case 97:
         s = "Black has good rook placement";
         break;
      case 98:
         s = "White has poor queen placement";
         break;
      case 99:
         s = "Black has poor queen placement";
         break;
      case 100:
         s = "White has good queen placement";
         break;
      case 101:
         s = "Black has good queen placement";
         break;
      case 102:
         s = "White has poor piece coordination";
         break;
      case 103:
         s = "Black has poor piece coordination";
         break;
      case 104:
         s = "White has good piece coordination";
         break;
      case 105:
         s = "Black has good piece coordination";
         break;
      case 106:
         s = "White has played the opening very poorly";
         break;
      case 107:
         s = "Black has played the opening very poorly";
         break;
      case 108:
         s = "White has played the opening poorly";
         break;
      case 109:
         s = "Black has played the opening poorly";
         break;
      case 110:
         s = "White has played the opening well";
         break;
      case 111:
         s = "Black has played the opening well";
         break;
      case 112:
         s = "White has played the opening very well";
         break;
      case 113:
         s = "Black has played the opening very well";
         break;
      case 114:
         s = "White has played the middlegame very poorly";
         break;
      case 115:
         s = "Black has played the middlegame very poorly";
         break;
      case 116:
         s = "White has played the middlegame poorly";
         break;
      case 117:
         s = "Black has played the middlegame poorly";
         break;
      case 118:
         s = "White has played the middlegame well";
         break;
      case 119:
         s = "Black has played the middlegame well";
         break;
      case 120:
         s = "White has played the middlegame very well";
         break;
      case 121:
         s = "Black has played the middlegame very well";
         break;
      case 122:
         s = "White has played the ending very poorly";
         break;
      case 123:
         s = "Black has played the ending very poorly";
         break;
      case 124:
         s = "White has played the ending poorly";
         break;
      case 125:
         s = "Black has played the ending poorly";
         break;
      case 126:
         s = "White has played the ending well";
         break;
      case 127:
         s = "Black has played the ending well";
         break;
      case 128:
         s = "White has played the ending very well";
         break;
      case 129:
         s = "Black has played the ending very well";
         break;
      case 130:
         s = "White has slight counterplay";
         break;
      case 131:
         s = "Black has slight counterplay";
         break;
      case 132:
         s = "White has moderate counterplay";
         break;
      case 133:
         s = "Black has moderate counterplay";
         break;
      case 134:
         s = "White has decisive counterplay";
         break;
      case 135:
         s = "Black has decisive counterplay";
         break;
      case 136:
         s = "White has moderate time control pressure";
         break;
      case 137:
         s = "Black has moderate time control pressure";
         break;
      case 138:
         s = "White has severe time control pressure";
         break;
      case 139:
         s = "Black has severe time control pressure";
         break;
      case 140:
         s = "With the idea...";
         break;
      case 141:
         s = "Aimed against...";
         break;
      case 142:
         s = "Better move";
         break;
      case 143:
         s = "Worse move";
         break;
      case 144:
         s = "Equivalent move";
         break;
      case 145:
         s = "Editor\'s Remark";
         break;
      case 146:
         s = "Novelty";
         break;
      case 147:
         s = "Weak point";
         break;
      case 148:
         s = "Endgame";
         break;
      case 149:
         s = "Line";
         break;
      case 150:
         s = "Diagonal";
         break;
      case 151:
         s = "White has a pair of Bishops";
         break;
      case 152:
         s = "Black has a pair of Bishops";
         break;
      case 153:
         s = "Bishops of opposite color";
         break;
      case 154:
         s = "Bishops of same color";
         break;
      case 155:
      case 156:
      case 157:
      case 158:
      case 159:
      case 160:
      case 161:
      case 162:
      case 163:
      case 164:
      case 165:
      case 166:
      case 167:
      case 168:
      case 169:
      case 170:
      case 171:
      case 172:
      case 173:
      case 174:
      case 175:
      case 176:
      case 177:
      case 178:
      case 179:
      case 180:
      case 181:
      case 182:
      case 183:
      case 184:
      case 185:
      case 186:
      case 187:
      case 188:
      case 189:
      case 196:
      case 197:
      case 198:
      case 199:
      case 200:
      default:
         s = null;
         break;
      case 190:
         s = "Etc.";
         break;
      case 191:
         s = "Double pawns";
         break;
      case 192:
         s = "Isolated pawn";
         break;
      case 193:
         s = "Connected pawns";
         break;
      case 194:
         s = "Hanging pawns";
         break;
      case 195:
         s = "Backwards pawn";
         break;
      case 201:
         s = "Diagram";
      }

      return s;
   }
}
