package com.nttdata.btask.interfaces;

import com.nttdata.domain.models.WalletDto;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

public interface WalletService {
  Multi<WalletDto> list();

  Uni<WalletDto> findByNroWallet(WalletDto walletDto);

  Uni<WalletDto> addWallet(WalletDto walletDto);

  Uni<WalletDto> updateWallet(WalletDto walletDto);

  Uni<WalletDto> deleteWallet(WalletDto walletDto);

  Uni<WalletDto> depositWallet(WalletDto walletDto);
  Uni<WalletDto> withdrawalWallet(WalletDto walletDto);
}
