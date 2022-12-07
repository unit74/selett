import React, { Fragment } from "react";
import {
  Modal,
  Spinner,
  useDisclosure,
  ModalOverlay,
  ModalContent,
} from "@chakra-ui/react";

function Lo(props) {
  return (
    <Fragment>
      <Modal
        isOpen={props.isOpen}
        background="transparent"
        borderStyle="none"
        border="transparent"
        isCentered
        // background="black"
        // style={{ backgroundColor: "transparent" }}
      >
        {/* <ModalOverlay /> */}
        <ModalContent w="min-content" background="transparent">
          <Spinner
            thickness="4px"
            speed="0.65s"
            emptyColor="gray.200"
            color="blue.700"
            size="xl"
            marginRight="20px"
          />
        </ModalContent>
      </Modal>
    </Fragment>
  );
}

export default Lo;
