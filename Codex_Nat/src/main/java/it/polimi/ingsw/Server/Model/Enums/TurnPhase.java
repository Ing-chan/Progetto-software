package it.polimi.ingsw.Server.Model.Enums;

public enum TurnPhase {



    hasToPlaceCard {
        @Override
        public String toString() {
            return "has to place a card";
        }
    },
    hasToDrawCard {
        @Override
        public String toString() {
            return "has to draw a card";
        }
    },
    hasToEndTurn {
        @Override
        public String toString() {
            return "has to end the turn";
        }
    }


}

